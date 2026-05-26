package wanted.jjsbd.lxpmvc.course.dto;

import java.util.List;

import wanted.jjsbd.lxpmvc.course.domain.Course;

public record CourseResponse(
	Long id,
	String title,
	String instructor,
	String description,
	String thumbnailUrl,
	Integer learnerCount,
	List<SectionResponse> curriculum

) {
	public static CourseResponse of(Course course) {
		// TODO: Curriculum 기능 구현 시 List.of()를 실제 컬리큘럼 리스트로 교체할 것.
		return new CourseResponse(
			course.getId(),
			course.getTitle(),
			course.getInstructorInfo().getName(),
			course.getDescription(),
			course.getThumbnailUrl(),
			course.getLearnerCount(),
			List.of()
		);
	}
}
