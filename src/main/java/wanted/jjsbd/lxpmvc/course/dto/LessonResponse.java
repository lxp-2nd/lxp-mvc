package wanted.jjsbd.lxpmvc.course.dto;

import wanted.jjsbd.lxpmvc.course.domain.MaterialType;

public record LessonResponse(
	String title,
	MaterialType type
) {
}
