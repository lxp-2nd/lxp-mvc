package wanted.jjsbd.lxpmvc.course.dto;

public record CourseSearchRequest(String q) {

	public CourseSearchRequest {
		q = q == null ? "" : q;
	}
}
