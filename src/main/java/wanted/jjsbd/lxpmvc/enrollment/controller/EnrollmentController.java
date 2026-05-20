package wanted.jjsbd.lxpmvc.enrollment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import wanted.jjsbd.lxpmvc.cart.dto.CartResponse;
import wanted.jjsbd.lxpmvc.common.MockLxpData;
import wanted.jjsbd.lxpmvc.common.domain.DomainValidator;
import wanted.jjsbd.lxpmvc.common.exception.CustomException;
import wanted.jjsbd.lxpmvc.enrollment.dto.CartEnrollmentRequest;
import wanted.jjsbd.lxpmvc.enrollment.dto.EnrollmentCompleteRequest;
import wanted.jjsbd.lxpmvc.enrollment.dto.EnrollmentCompleteResponse;
import wanted.jjsbd.lxpmvc.enrollment.dto.EnrollmentRequest;
import wanted.jjsbd.lxpmvc.enrollment.dto.EnrollmentResponse;
import wanted.jjsbd.lxpmvc.enrollment.dto.LearningRequest;
import wanted.jjsbd.lxpmvc.enrollment.dto.LearningResponse;
import wanted.jjsbd.lxpmvc.enrollment.service.EnrollmentService;
import wanted.jjsbd.lxpmvc.member.domain.Member;

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
		@PathVariable String courseId,
		@SessionAttribute(name = "loginMember", required = true) Member loginMember
	) {

		try {
			// 1. 로그인 사용자인지 확인후 비로그인인 경우 login 화면으로 redirect
			DomainValidator.validateNotNull(loginMember);
			// 1-1. member객체의 ID값이 Long인지 확인 -> 미구현 사항(5/20)
			// DomainValidator.validateNotNull(loginMember.getId());

			// 2. courseId가 null인지에 대해 확인
			DomainValidator.validateNotBlank(courseId);
			// 여기 분기의 경우에는 1의 경우 로그인으로, 2의 경우 원래 강의상세 페이지에 두면 될듯합니다.

			EnrollmentRequest request = new EnrollmentRequest(1L, Long.valueOf(courseId));
			enrollmentService.enroll(request);

			return "redirect:/enrollment/complete?courseId=" + request.courseId();
		} catch (CustomException e) {
			// 로그인으로 분기에 대한 것을 기준 잡기가 애매함..
			return "redirect:/member/login";
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 장바구니에서 선택한 강의 수강 신청
	 * @param request
	 * @param model
	 * @return
	 */
	@PostMapping("/cart/enroll")
	public String enrollCart(CartEnrollmentRequest request, Model model) {
		if (request.courseIds().isEmpty()) {
			addCartModel(model);
			model.addAttribute("validationError", "신청할 강의를 1개 이상 선택해주세요.");
			return "cart/index";
		}

		String courseId = request.courseIds().get(0);
		return "redirect:/enrollment/complete?courseId=" + courseId;
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

	private void addCartModel(Model model) {
		CartResponse cart = mockData.cart();

		model.addAttribute("title", "장바구니");
		model.addAttribute("cart", cart);
		model.addAttribute("cartItems", cart.cartItems());
		model.addAttribute("cartCount", cart.cartCount());
		model.addAttribute("selectedCount", cart.selectedCount());
	}
}
