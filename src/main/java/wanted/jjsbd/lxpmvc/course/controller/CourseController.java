package wanted.jjsbd.lxpmvc.course.controller;

import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import wanted.jjsbd.lxpmvc.common.MockLxpData;
import wanted.jjsbd.lxpmvc.course.dto.CourseResponse;
import wanted.jjsbd.lxpmvc.course.dto.CourseSearchRequest;

@Controller
public class CourseController {

	private final MockLxpData mockData;

	public CourseController(MockLxpData mockData) {
		this.mockData = mockData;
	}

	@GetMapping("/")
	public String home() {
		return "redirect:/courses";
	}

	@GetMapping("/courses")
	public String courses(CourseSearchRequest request, Model model) {
		String query = request.q();
		List<CourseResponse> filteredCourses = mockData.courses().stream()
			.filter(course -> query.isBlank()
					|| course.title().toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT)))
			.toList();

		model.addAttribute("title", "강의 목록");
		model.addAttribute("query", query);
		model.addAttribute("courses", filteredCourses);
		model.addAttribute("cartCount", mockData.cartCourses().size());
		return "course/list";
	}

	@GetMapping("/courses/{courseId}")
	public String courseDetail(@PathVariable String courseId, Model model) {
		CourseResponse course = mockData.findCourse(courseId);

		model.addAttribute("title", "강의 상세");
		model.addAttribute("course", course);
		model.addAttribute("cartCount", mockData.cartCourses().size());
		return "course/detail";
	}
}
