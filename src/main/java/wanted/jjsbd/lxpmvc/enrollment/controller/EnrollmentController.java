package wanted.jjsbd.lxpmvc.enrollment.controller;

import java.util.stream.Collectors;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import wanted.jjsbd.lxpmvc.common.MockLxpData;
import wanted.jjsbd.lxpmvc.common.domain.DomainValidator;
import wanted.jjsbd.lxpmvc.common.exception.CustomException;
import wanted.jjsbd.lxpmvc.common.exception.ErrorCode;
import wanted.jjsbd.lxpmvc.course.domain.Course;
import wanted.jjsbd.lxpmvc.course.service.CourseService;
import wanted.jjsbd.lxpmvc.enrollment.domain.Enrollment;
import wanted.jjsbd.lxpmvc.enrollment.dto.CartEnrollmentRequest;
import wanted.jjsbd.lxpmvc.enrollment.dto.EnrollmentCompleteRequest;
import wanted.jjsbd.lxpmvc.enrollment.dto.EnrollmentCompleteResponse;
import wanted.jjsbd.lxpmvc.enrollment.dto.EnrollmentCourseResponse;
import wanted.jjsbd.lxpmvc.enrollment.dto.EnrollmentRequest;
import wanted.jjsbd.lxpmvc.enrollment.dto.EnrollmentResponse;
import wanted.jjsbd.lxpmvc.enrollment.dto.LearningRequest;
import wanted.jjsbd.lxpmvc.enrollment.dto.LearningResponse;
import wanted.jjsbd.lxpmvc.enrollment.service.EnrollmentService;
import wanted.jjsbd.lxpmvc.member.domain.AuthInfo;

@Controller
public class EnrollmentController {

	private final EnrollmentService enrollmentService;
	private final MockLxpData mockData;

	// @Autowired 생성자
	public EnrollmentController(MockLxpData mockData, EnrollmentService enrollmentService) {
		this.mockData = mockData;
		this.enrollmentService = enrollmentService;
	}

	/**
	 * 수강신청 완료 화면 조회
	 * @param courseId
	 * @param model
	 * @return
	 */
	@GetMapping("/enroll/complete")
	public String enrollComplete(
			@RequestParam String courseId,
			@AuthenticationPrincipal AuthInfo authInfo,
			Model model
	) {
		// 1. 단일 또는 다중으로 들어옴(courseId) courseId=1 or courseId=1,2,3
		String[] courseIdArray = courseId.split(",");
		Long firstCourseId;
		try {
			firstCourseId = Long.valueOf(courseIdArray[0]);
		} catch (RuntimeException e) {
			throw new CustomException(ErrorCode.ENROLLMENT_NOT_FOUND, "/cart");
		}



		// 2. 등록된 수강에서 조회
		Enrollment savedEnrollment = enrollmentService.getCompletedEnrollment(
			authInfo.memberId(),
			firstCourseId
		);

		// Enrollment 객체를 통해 연관된 Course의 제목을 가져옴
		String firstCourseTitle = savedEnrollment.getCourse().getTitle();

		String displayTitle;

		// 3. 단일 or 다중 분기처리
		if (courseIdArray.length == 1) {
			// [단일 신청] 강의 상세 -> 수강 신청 (또는 장바구니에서 1개만 신청)
			displayTitle = firstCourseTitle; // 도메인의 실제 강의명 메서드 사용
		} else {
			// [다중 신청]
			displayTitle = firstCourseTitle + " 외 " + (courseIdArray.length - 1) + "건";
		}

		// 3. 화면에 전달할 record
		EnrollmentCompleteResponse enrollment = new EnrollmentCompleteResponse(
			savedEnrollment.getId().toString(),
			String.valueOf(firstCourseId),
			displayTitle,
			"신청완료"
		);

		model.addAttribute("title", "수강 신청 완료");
		model.addAttribute("enrollment", enrollment);
		model.addAttribute("cartCount", mockData.cartCourses().size());
		return "enrollment/complete";
	}

	/**
	 * 강의상세 화면에서 단일 강의 수강 신청
	 * @param courseId
	 * @return
	 */
	@PostMapping("/courses/{courseId}/enroll")
	public String enroll(
		@PathVariable Long courseId,
		@AuthenticationPrincipal AuthInfo authInfo
	) {
		// 1. courseId가 null인지 확인
		// parameter에서 이미 Long이 아닌 경우에 checking 되긴 하지만 한번 더 체크(필요있나?)
		// -> handleCustomException로 인해서 error/error로 분기(원래는 강의 목록으로 분기하려 함)
		DomainValidator.validateNotBlank(String.valueOf(courseId));

		EnrollmentRequest request = new EnrollmentRequest(authInfo.memberId(), courseId);
		enrollmentService.enroll(request);

		// 성공 -> 강의 완료 화면
		return "redirect:/enroll/complete?courseId=" + request.courseId();
	}

	/**
	 * 장바구니에서 선택한 강의 수강 신청
	 * @param request
	 * @param authInfo
	 * @param redirectAttributes
	 * @return
	 */
	@PostMapping("/cart/enroll")
	public String enrollCart(
		CartEnrollmentRequest request,
		@AuthenticationPrincipal AuthInfo authInfo,
		RedirectAttributes redirectAttributes
	) {
		// 1. 서버 측 안전장치 (프론트에서 JS로 막고 있지만 2차 검증)
		if (request.isEmpty()) {
			redirectAttributes.addFlashAttribute("validationError", "신청할 강의를 1개 이상 선택해주세요.");
			return "redirect:/cart"; // 모델 데이터를 다시 조회하지 않기 위해 리다이렉트 처리 (PRG 패턴)
		}

		// 2. 다중 수강신청 서비스 호출
		enrollmentService.enrollCart(authInfo.memberId(), request.courseIds());

		// 3. 완료 화면 리다이렉트를 위해 courseId들을 콤마로 연결
		String joinedCourseIds = request.courseIds().stream()
			.map(String::valueOf)
			.collect(Collectors.joining(","));

		return "redirect:/enroll/complete?courseId=" + joinedCourseIds;
	}

	/**
	 * 수강 목록 화면 조회
	 * @param model
	 * @param authInfo
	 * @return
	 */
	@GetMapping("/enrollment")
	public String enrollment(
		Model model,
		@AuthenticationPrincipal AuthInfo authInfo,
		@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		// EnrollmentResponse enrollment = mockData.enrollment();

		// 1. 내강의 목록 조회할 회원ID(learnerId)로 조회
		Page<EnrollmentCourseResponse> enrollmentPage =
			enrollmentService.getActiveEnrollments(authInfo.memberId(), pageable);

		model.addAttribute("title", "수강 목록");
		model.addAttribute("enrollments", enrollmentPage.getContent());
		model.addAttribute("pageInfo", enrollmentPage);
		model.addAttribute("cartCount", mockData.cartCourses().size());
		return "enrollment/list";
	}

	/**
	 * 수강 상세 및 학습 화면 조회
	 * @param sectionId
	 * @param courseMaterialId
	 * @param courseId
	 * @param model
	 * @return
	 */
	@GetMapping("/learn/{courseId}")
	public String learn(
		@RequestParam(required = false, defaultValue = "0") int sectionId,
		@RequestParam(required = false, defaultValue = "0") int courseMaterialId,
		@PathVariable String courseId,
		Model model
	) {
		LearningRequest request = new LearningRequest(courseId, sectionId, courseMaterialId);
		LearningResponse learning = mockData.learning(request.courseId(), request.sectionId(),
			request.courseMaterialId());

		model.addAttribute("title", "수강 상세 / 수강 화면");
		model.addAttribute("learning", learning);
		model.addAttribute("selectedCourseMaterial", learning.selectedCourseMaterial());
		model.addAttribute("cartCount", mockData.cartCourses().size());
		return "enrollment/detail";
	}

	/*private void addCartModel(Model model) {
		CartResponse cart = mockData.cart();

		model.addAttribute("title", "장바구니");
		model.addAttribute("cart", cart);
		model.addAttribute("cartItems", cart.cartItems());
		// model.addAttribute("cartCount", cart.cartCount());
		// model.addAttribute("selectedCount", cart.selectedCount());
	}*/
}

