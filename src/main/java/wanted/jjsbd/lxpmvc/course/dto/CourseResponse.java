package wanted.jjsbd.lxpmvc.course.dto;

import java.util.List;

public record CourseResponse(
	Long id,
	String title, /// 강의명
	String instructor,
	String description,
	Integer leanerCount,
	List<SectionResponse> curriculum ///하위 커리큘럼(섹션+자료) 목록

) {
}
