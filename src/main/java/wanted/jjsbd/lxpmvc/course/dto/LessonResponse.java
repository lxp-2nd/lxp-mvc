package wanted.jjsbd.lxpmvc.course.dto;

import wanted.jjsbd.lxpmvc.course.domain.MaterialType;

public record LessonResponse(
	String title, ///강의자료 제목
	MaterialType type
) {
}
