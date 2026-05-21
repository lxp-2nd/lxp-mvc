package wanted.jjsbd.lxpmvc.course.dto;

import java.util.List;

public record CourseResponse(
	String id,
	String title,
	String instructor,
	String description,
	List<SectionResponse> curriculum,
	int learnerCount
) {
}
