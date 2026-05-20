package wanted.jjsbd.lxpmvc.enrollment.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import wanted.jjsbd.lxpmvc.common.MockLxpData;
import wanted.jjsbd.lxpmvc.common.exception.CustomException;
import wanted.jjsbd.lxpmvc.common.exception.ErrorCode;
import wanted.jjsbd.lxpmvc.course.domain.Course;
import wanted.jjsbd.lxpmvc.course.dto.CourseResponse;
import wanted.jjsbd.lxpmvc.enrollment.domain.Enrollment;
import wanted.jjsbd.lxpmvc.enrollment.dto.EnrollmentRequest;
import wanted.jjsbd.lxpmvc.enrollment.repository.EnrollmentRepository;
import wanted.jjsbd.lxpmvc.member.domain.Member;

@Service
public class EnrollmentService {

	private final EnrollmentRepository enrollmentRepository;
	// private final CourseRepository courseRepository;
	// private final MemberRepository memberRepository;

	// 생성자 패턴(@Autowired 제거)
	public EnrollmentService(EnrollmentRepository enrollmentRepository) {
		this.enrollmentRepository = enrollmentRepository;
	}

	// 수강신청(DB 저장)
	public Enrollment createEnrollment(Enrollment enrollment) {
		Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
		return savedEnrollment;
		// return enrollmentRepository.save(enrollment); 로 축약 가능
	}

	/**
	 * 수강신청
	 * @return Long enrollmentId
	 */
	@Transactional
	public Long enroll(EnrollmentRequest request) {

		// [비즈니스 로직] 중복 수강인지 확인
		if (enrollmentRepository.existsByMemberIdAndCourseId(request.learnerId(), request.courseId())) {
			throw new CustomException(ErrorCode.ENROLLMENT_ALREADY_EXISTS_SKIPPED);
		}

		// mockdata
		MockLxpData mockData = new MockLxpData();

		// 회원 조회
		// memberRepository 구현 후 추후 추가 수정
		// Member learner = memberRepository.findById(learnerId);
		Member learner = new Member();

		// 강의 조회
		// courseRepository 구현 후 추후 추가 수정
		// Course course = courseRepository.findById(courseId);
		CourseResponse cr = mockData.findCourse("0");
		Course course = Course.createCourse(learner, cr.title(), cr.description());

		// Enrollment 생성
		Enrollment enrollment = Enrollment.createEnrollment(learner, course);

		try {
			Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
			enrollmentRepository.flush();

			return savedEnrollment.getId();
		} catch (DataIntegrityViolationException e) {
			throw new CustomException(ErrorCode.ENROLLMENT_ALREADY_EXISTS_SKIPPED);
		}
	}

}
