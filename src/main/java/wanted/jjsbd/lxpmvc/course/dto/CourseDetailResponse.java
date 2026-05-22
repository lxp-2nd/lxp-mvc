package wanted.jjsbd.lxpmvc.course.dto;

import java.util.List;

import wanted.jjsbd.lxpmvc.course.domain.Course;
import wanted.jjsbd.lxpmvc.course.domain.Section;

public record CourseDetailResponse(
	Long id,
	String title,
	String description,
	String instructorName,
	List<SectionResponse> curriculum
) {
	public static CourseDetailResponse of(Course course) {
		// Course -> Section -> Material 구조를 DTO 리스트로 변환
		List<SectionResponse> curriculumList = course.getSections()
			.stream()
			.map(CourseDetailResponse::getSectionResponse)
			.toList();

		return new CourseDetailResponse(
			course.getId(),
			course.getTitle(),
			course.getDescription(),
			course.getInstructor().getNickname(),
			curriculumList
		);
	}

	private static SectionResponse getSectionResponse(Section section) {
		return new SectionResponse(section.getTitle(), section.getMaterials()
			.stream()
			.map(material ->
				new LessonResponse(material.getTitle(), material.getMaterialType()))
			.toList());
	}

}
