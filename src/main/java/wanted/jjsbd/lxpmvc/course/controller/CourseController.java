package wanted.jjsbd.lxpmvc.course.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import lombok.RequiredArgsConstructor;
import wanted.jjsbd.lxpmvc.course.dto.CourseDetailResponse;
import wanted.jjsbd.lxpmvc.course.dto.CourseResponse;
import wanted.jjsbd.lxpmvc.course.dto.CourseSearchRequest;
import wanted.jjsbd.lxpmvc.course.service.CourseService;
import wanted.jjsbd.lxpmvc.member.domain.AuthInfo;

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
		Model model,
		@AuthenticationPrincipal AuthInfo authInfo
	) {
		Long memberId = (authInfo != null) ? authInfo.memberId() : null;

		List<CourseResponse> courseList = courseService.getCourses(request, memberId);

		model.addAttribute("title", "강의 목록");
		model.addAttribute("query", request.q());

		model.addAttribute("courses", courseList);

		return "course/list";

	}

	@GetMapping("/courses/{courseId}")
	public String courseDetail(@PathVariable Long courseId, Model model) {
		CourseDetailResponse courseDetail = courseService.getCourseDetails(courseId);

		model.addAttribute("title", "강의 상세");
		model.addAttribute("course", courseDetail);

		return "course/detail";
	}
}
