package wanted.jjsbd.lxpmvc.course.dto;

import java.util.List;

public record SectionResponse(
	String title, ///섹션 제목
	List<LessonResponse> lessons
) {
}
