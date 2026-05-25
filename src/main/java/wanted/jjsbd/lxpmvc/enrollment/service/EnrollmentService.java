package wanted.jjsbd.lxpmvc.enrollment.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wanted.jjsbd.lxpmvc.cart.service.CartService;
import wanted.jjsbd.lxpmvc.common.exception.CustomException;
import wanted.jjsbd.lxpmvc.common.exception.ErrorCode;
import wanted.jjsbd.lxpmvc.course.domain.Course;
import wanted.jjsbd.lxpmvc.course.repository.CourseRepository;
import wanted.jjsbd.lxpmvc.enrollment.domain.Enrollment;
import wanted.jjsbd.lxpmvc.enrollment.dto.EnrollmentRequest;
import wanted.jjsbd.lxpmvc.enrollment.repository.EnrollmentRepository;
import wanted.jjsbd.lxpmvc.member.domain.Member;
import wanted.jjsbd.lxpmvc.member.repository.MemberRepository;

@Service
public class EnrollmentService {

	private final EnrollmentRepository enrollmentRepository;
	private final CourseRepository courseRepository;
	private final MemberRepository memberRepository;
	private final CartService cartService;

	// 생성자 패턴(@Autowired 제거)
	public EnrollmentService(
		EnrollmentRepository enrollmentRepository,
		MemberRepository memberRepository,
		CourseRepository courseRepository,
		CartService cartService
	) {
		this.enrollmentRepository = enrollmentRepository;
		this.memberRepository = memberRepository;
		this.courseRepository = courseRepository;
		this.cartService = cartService;
	}

	/**
	 * 수강신청
	 * @return Long enrollmentId
	 */
	@Transactional
	public Long enroll(EnrollmentRequest request) {

		// 1. 회원(Learner) 유효성 검증: 존재 여부 및 Soft Delete 여부 확인
		// ErrorCode 상황: 회원이 회원ID로 조회되지 않는 경우
		Member learner = memberRepository.findById(request.learnerId())
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND, "/courses/" + request.courseId()));
		if (learner.isDeleted()) {
			throw new CustomException(ErrorCode.MEMBER_ALREADY_WITHDRAWN); // 탈퇴한 회원 예외
		}

		// 2. 강의(Course) 유효성 검증: 존재 여부 및 Soft Delete 여부 확인
		// ErrorCode 상황: 강의가 강의ID로 조회되지 않는 경우
		Course course = courseRepository.findById(request.courseId())
			.orElseThrow(() -> new CustomException(ErrorCode.COURSE_NOT_FOUND, "/courses/" + request.courseId()));
		if (course.isDeleted()) {
			throw new CustomException(ErrorCode.COURSE_ALREADY_WITHDRAWN); // 폐강된 강의 예외
		}

		// 3. 회원과 강의는 있는 것으로 확인 수강 로직 진행
		// upsert로 진행(soft delete로 수강-회원이 남아있을 경우 재수강이 불가 함)
		Optional<Enrollment> enrollmentOpt = enrollmentRepository.findByLearnerIdAndCourseId(
			request.learnerId(), request.courseId());

		if (enrollmentOpt.isPresent()) {
			Enrollment enrollment = enrollmentOpt.get();

			// 3-1. 이미 정상적으로 수강 중인 경우 (중복 신청 방어)
			// [비즈니스 로직 1] 중복 수강인지 확인(deletedAt null여부로 확인)
			if (!enrollment.isDeleted()) {
				throw new CustomException(
					ErrorCode.ENROLLMENT_ALREADY_EXISTS_SKIPPED, "/courses/" + request.courseId());
			}

			// 3-2. Soft Delete된 데이터가 있는 경우 -> 복구 (Update)
			enrollment.restore();
			return enrollment.getId();
		} else {
			// 3-3. 기존 데이터가 아예 없는 경우 -> 신규 생성 (Insert)
			Enrollment enrollment = Enrollment.createEnrollment(learner, course);

			try {
				Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
				enrollmentRepository.flush();

				return savedEnrollment.getId();
			} catch (DataIntegrityViolationException e) {
				throw new CustomException(
					ErrorCode.ENROLLMENT_ALREADY_EXISTS_SKIPPED, "/courses/" + request.courseId());
			} catch (Exception e) {
				// JPA에서의 문제 발생 및 기타 에러
				throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "/courses/" + request.courseId());
			}
		}
	}

	/**
	 * 장바구니 다중 수강신청
	 * @param learnerId 회원 ID
	 * @param courseIds 신청할 강의 ID 리스트
	 * @return 생성 및 복구된 enrollmentId 리스트
	 */
	@Transactional
	public List<Long> enrollCart(Long learnerId, List<Long> courseIds) {
		List<Long> enrolledIds = new ArrayList<>();

		for (Long courseId : courseIds) {
			// 단일 수강신청 로직(회원/강의 검증, 중복체크, 복구/생성) 재사용
			EnrollmentRequest request = new EnrollmentRequest(learnerId, courseId);
			Long enrollmentId = enroll(request);
			enrolledIds.add(enrollmentId);
		}

		// 장바구니 삭제(수강생ID, 강의ID들(단일/복수))
		cartService.deleteCartItemsByCourseIds(learnerId, courseIds);

		return enrolledIds;
	}

	/**
	 * 완료 화면을 위한 수강 이력 단건 조회
	 * @param learnerId 회원 ID
	 * @param courseId 강의 ID
	 * @return 수강 이력 엔티티
	 */
	@Transactional(readOnly = true)
	public Enrollment getCompletedEnrollment(Long learnerId, Long courseId) {
		return enrollmentRepository.findByLearnerIdAndCourseId(learnerId, courseId)
			.filter(enrollment -> !enrollment.isDeleted()) // 소프트 딜리트되지 않은 정상 수강 건인지 확인
			.orElseThrow(() -> new CustomException(
				ErrorCode.ENROLLMENT_NOT_FOUND
			));
	}

}
