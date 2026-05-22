package wanted.jjsbd.lxpmvc.course.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import lombok.RequiredArgsConstructor;
import wanted.jjsbd.lxpmvc.course.dto.CourseDetailResponse;
import wanted.jjsbd.lxpmvc.course.dto.CourseResponse;
import wanted.jjsbd.lxpmvc.course.dto.CourseSearchRequest;
import wanted.jjsbd.lxpmvc.course.service.CourseService;

@Controller
@RequiredArgsConstructor
public class CourseController {

	private final CourseService courseService;

	@GetMapping("/")
	public String home() {
		return "redirect:/courses";
	}

	@GetMapping("/courses")
	public String courses(
		CourseSearchRequest request,
		@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
		Model model
	) {
		Page<CourseResponse> coursePage = courseService.getCourses(request, pageable);

		model.addAttribute("title", "강의 목록");
		model.addAttribute("query", request.q());

		model.addAttribute("courses", coursePage.getContent());

		return "course/list";

	}

	@GetMapping("/courses/{courseId}")
	public String courseDetail(@PathVariable Long courseId, Model model) {
		// 1. Service를 호출하여 식별자(ID)에 해당하는 강의 상세 데이터(DTO)를 가져옵니다.
		CourseDetailResponse courseDetail = courseService.getCourseDetails(courseId);

		// 2. Thymeleaf 템플릿에서 사용할 수 있도록 Model에 데이터를 담아줍니다.
		model.addAttribute("title", "강의 상세");
		model.addAttribute("course", courseDetail); // detail.html 본문 바인딩용

		// 3. 뷰 템플릿 반환
		return "course/detail";
	}
}
