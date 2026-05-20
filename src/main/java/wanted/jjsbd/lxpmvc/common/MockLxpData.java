package wanted.jjsbd.lxpmvc.common;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import wanted.jjsbd.lxpmvc.cart.dto.CartItemResponse;
import wanted.jjsbd.lxpmvc.cart.dto.CartResponse;
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
			.filter(course -> course.id().equals(courseId))
			.findFirst()
			.orElse(courses().get(0));
	}

	public List<CourseResponse> courses() {
		return List.of(
			new CourseResponse("frontend-basic", "프론트엔드 입문", "강사명", "HTML, CSS, JavaScript 기초를 다룹니다.",
				curriculum(), 1),
			new CourseResponse("data-analysis", "데이터 분석 기초", "강사명", "비즈니스 데이터를 읽고 시각화하는 방법을 익힙니다.",
				curriculum(), 4),
			new CourseResponse("service-planning", "서비스 기획 MVP", "강사명", "서비스 기획과 MVP 화면 설계를 학습합니다.",
				curriculum(), 6),
			new CourseResponse("backend-api", "백엔드 API 설계", "강사명", "REST API와 서버 설계 흐름을 정리합니다.",
				curriculum(), 33),
			new CourseResponse("ux-research", "UX 리서치 시작하기", "강사명", "사용자 인터뷰와 리서치 결과 정리를 연습합니다.",
				curriculum(), 2),
			new CourseResponse("sql-basic", "SQL 기본", "강사명", "데이터 조회와 집계에 필요한 SQL 기본기를 배웁니다.",
				curriculum(), 0));
	}

	public List<CourseResponse> cartCourses() {
		Set<String> cartCourseIds = Set.of("frontend-basic", "service-planning", "sql-basic");
		return courses().stream()
			.filter(course -> cartCourseIds.contains(course.id()))
			.toList();
	}

	public CartResponse cart() {
		List<CartItemResponse> cartItems = cartCourses().stream()
			.map(course -> new CartItemResponse("cart-" + course.id(), course.id(), course.title(),
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
		return new EnrollmentCompleteResponse("enrollment-" + course.id(), course.id(), course.title(), "신청완료");
	}

	public LearningResponse learning(String courseId, int sectionId, int courseMaterialId) {
		CourseResponse course = findCourse(courseId);
		List<LearningSectionResponse> sections = course.curriculum().stream()
			.map(section -> new LearningSectionResponse(section.title(), section.lessons().stream()
				.map(lesson -> new LearningCourseMaterialResponse(lesson.title(), lesson.type()))
				.toList()))
			.toList();

		return new LearningResponse("enrollment-" + course.id(), course.id(), course.title(), sections,
			selectedCourseMaterial(sections, sectionId, courseMaterialId));
	}

	public MemberResponse member() {
		return new MemberResponse("홍길동", "name@example.com");
	}

	private List<SectionResponse> curriculum() {
		return List.of(
			new SectionResponse("섹션 1. 강의 소개",
				List.of(new LessonResponse("강의자료 제목", "동영상"), new LessonResponse("강의자료 제목", "문서"))),
			new SectionResponse("섹션 2. 핵심 개념",
				List.of(new LessonResponse("강의자료 제목", "동영상"), new LessonResponse("강의자료 제목", "문서"))));
	}

	private EnrollmentCourseResponse enrollmentCourse(String courseId) {
		CourseResponse course = findCourse(courseId);
		return new EnrollmentCourseResponse("enrollment-" + course.id(), course.id(), course.title(),
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
