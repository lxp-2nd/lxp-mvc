package wanted.jjsbd.lxpmvc.enrollment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import wanted.jjsbd.lxpmvc.cart.common.MockLxpData;
import wanted.jjsbd.lxpmvc.cart.dto.CartResponse;
import wanted.jjsbd.lxpmvc.enrollment.dto.CartEnrollmentRequest;
import wanted.jjsbd.lxpmvc.enrollment.dto.EnrollmentCompleteRequest;
import wanted.jjsbd.lxpmvc.enrollment.dto.EnrollmentCompleteResponse;
import wanted.jjsbd.lxpmvc.enrollment.dto.EnrollmentRequest;
import wanted.jjsbd.lxpmvc.enrollment.dto.EnrollmentResponse;
import wanted.jjsbd.lxpmvc.enrollment.dto.LearningRequest;
import wanted.jjsbd.lxpmvc.enrollment.dto.LearningResponse;

@Controller
public class EnrollmentController {

	private final MockLxpData mockData;

	public EnrollmentController(MockLxpData mockData) {
		this.mockData = mockData;
	}

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

	@PostMapping("/courses/{courseId}/enroll")
	public String enroll(@PathVariable String courseId) {
		EnrollmentRequest request = new EnrollmentRequest(courseId);
		return "redirect:/enroll/complete?courseId=" + request.courseId();
	}

	@PostMapping("/cart/enroll")
	public String enrollCart(CartEnrollmentRequest request, Model model) {
		if (request.courseIds().isEmpty()) {
			addCartModel(model);
			model.addAttribute("validationError", "신청할 강의를 1개 이상 선택해주세요.");
			return "cart/index";
		}

		String courseId = request.courseIds().get(0);
		return "redirect:/enroll/complete?courseId=" + courseId;
	}

	@GetMapping("/enrollment")
	public String enrollment(Model model) {
		EnrollmentResponse enrollment = mockData.enrollment();

		model.addAttribute("title", "수강 목록");
		model.addAttribute("enrollments", enrollment.enrollments());
		model.addAttribute("cartCount", mockData.cartCourses().size());
		return "enrollment/list";
	}

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
