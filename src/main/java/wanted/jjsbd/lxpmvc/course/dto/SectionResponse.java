package wanted.jjsbd.lxpmvc.course.dto;

import java.util.List;

public record SectionResponse(String title, List<LessonResponse> lessons) {
}
