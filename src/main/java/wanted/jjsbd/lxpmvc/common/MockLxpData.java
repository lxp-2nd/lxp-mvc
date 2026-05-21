package wanted.jjsbd.lxpmvc.common;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import wanted.jjsbd.lxpmvc.cart.dto.CartItemResponse;
import wanted.jjsbd.lxpmvc.cart.dto.CartResponse;
import wanted.jjsbd.lxpmvc.course.domain.MaterialType;
import wanted.jjsbd.lxpmvc.course.dto.CourseResponse;
import wanted.jjsbd.lxpmvc.course.dto.LessonResponse;
import wanted.jjsbd.lxpmvc.course.dto.SectionResponse;
import wanted.jjsbd.lxpmvc.enrollment.dto.EnrollmentCompleteResponse;
import wanted.jjsbd.lxpmvc.enrollment.dto.EnrollmentCourseResponse;
import wanted.jjsbd.lxpmvc.enrollment.dto.EnrollmentResponse;
import wanted.jjsbd.lxpmvc.enrollment.dto.LearningCourseMaterialResponse;
import wanted.jjsbd.lxpmvc.enrollment.dto.LearningResponse;
import wanted.jjsbd.lxpmvc.enrollment.dto.LearningSectionResponse;
import wanted.jjsbd.lxpmvc.member.dto.MemberResponse;

@Component
public class MockLxpData {

	public CourseResponse findCourse(String courseId) {
		return courses().stream()
			.filter(course -> String.valueOf(course.id()).equals(courseId))
			.findFirst()
			.orElse(courses().get(0));
	}

	public List<CourseResponse> courses() {
		return List.of(
			new CourseResponse(1L, "프론트엔드 입문", "강사명", "HTML, CSS, JavaScript 기초를 다룹니다.",
				1, curriculum()),
			new CourseResponse(1L, "백엔드 입문", "강사명", "스프링",
				2, curriculum()
			)
		);

	}

	public List<CourseResponse> cartCourses() {
		Set<String> cartCourseIds = Set.of("frontend-basic", "service-planning", "sql-basic");
		return courses().stream()
			.filter(course -> cartCourseIds.contains(String.valueOf(course.id())))
			.toList();
	}

	public CartResponse cart() {
		List<CartItemResponse> cartItems = cartCourses().stream()
			.map(course -> new CartItemResponse("cart-" + course.id(), String.valueOf(course.id()), course.title(),
				course.instructor(), true))
			.toList();
		int selectedCount = (int)cartItems.stream()
			.filter(CartItemResponse::selected)
			.count();

		return new CartResponse(cartItems, cartItems.size(), selectedCount);
	}

	public EnrollmentResponse enrollment() {
		return new EnrollmentResponse(List.of(
			enrollmentCourse("service-planning"),
			enrollmentCourse("frontend-basic"),
			enrollmentCourse("sql-basic")));
	}

	public EnrollmentCompleteResponse enrollmentComplete(String courseId) {
		CourseResponse course = findCourse(courseId);
		return new EnrollmentCompleteResponse("enrollment-" + course.id(), String.valueOf(course.id()), course.title(),
			"신청완료");
	}

	public LearningResponse learning(String courseId, int sectionId, int courseMaterialId) {
		CourseResponse course = findCourse(courseId);
		List<LearningSectionResponse> sections = course.curriculum().stream()
			.map(section -> new LearningSectionResponse(section.title(), section.lessons().stream()
				.map(lesson -> new LearningCourseMaterialResponse(lesson.title(), lesson.type()))
				.toList()))
			.toList();

		return new LearningResponse("enrollment-" + course.id(), String.valueOf(course.id()), course.title(), sections,
			selectedCourseMaterial(sections, sectionId, courseMaterialId));
	}

	public MemberResponse member() {
		return new MemberResponse("홍길동", "name@example.com");
	}

	private List<SectionResponse> curriculum() {
		return List.of(
			new SectionResponse("섹션 1. 강의 소개",
				List.of(new LessonResponse("강의자료 제목", MaterialType.VIDEO),
					new LessonResponse("강의자료 제목", MaterialType.DOCUMENT))),
			new SectionResponse("섹션 2. 핵심 개념",
				List.of(new LessonResponse("강의자료 제목", MaterialType.DOCUMENT),
					new LessonResponse("강의자료 제목", MaterialType.DOCUMENT))));
	}

	private EnrollmentCourseResponse enrollmentCourse(String courseId) {
		CourseResponse course = findCourse(courseId);
		return new EnrollmentCourseResponse("enrollment-" + course.id(), String.valueOf(course.id()), course.title(),
			course.instructor(), "신청됨");
	}

	private LearningCourseMaterialResponse selectedCourseMaterial(List<LearningSectionResponse> sections,
		int sectionId, int courseMaterialId) {
		if (sectionId < 0 || sectionId >= sections.size()) {
			return sections.get(0).courseMaterials().get(0);
		}

		List<LearningCourseMaterialResponse> courseMaterials = sections.get(sectionId).courseMaterials();
		if (courseMaterialId < 0 || courseMaterialId >= courseMaterials.size()) {
			return courseMaterials.get(0);
		}

		return courseMaterials.get(courseMaterialId);
	}
}





