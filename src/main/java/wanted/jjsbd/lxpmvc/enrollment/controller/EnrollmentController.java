package wanted.jjsbd.lxpmvc.enrollment.controller;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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
import wanted.jjsbd.lxpmvc.enrollment.dto.CartEnrollmentRequest;
import wanted.jjsbd.lxpmvc.enrollment.dto.EnrollmentCompleteRequest;
import wanted.jjsbd.lxpmvc.enrollment.dto.EnrollmentCompleteResponse;
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

	@Autowired
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
	public String enrollComplete(@RequestParam(required = false, defaultValue = "service-planning") String courseId,
		Model model) {
		EnrollmentCompleteRequest request = new EnrollmentCompleteRequest(courseId);
		EnrollmentCompleteResponse enrollment = mockData.enrollmentComplete(request.courseId());

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
		return "redirect:/enrollment/complete?courseId=" + request.courseId();
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

		return "redirect:/enrollment/complete?courseId=" + joinedCourseIds;
	}

	/**
	 * 수강 목록 화면 조회
	 * @param model
	 * @return
	 */
	@GetMapping("/enrollment")
	public String enrollment(Model model) {
		EnrollmentResponse enrollment = mockData.enrollment();

		model.addAttribute("title", "수강 목록");
		model.addAttribute("enrollments", enrollment.enrollments());
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

