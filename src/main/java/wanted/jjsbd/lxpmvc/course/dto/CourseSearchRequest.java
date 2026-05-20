package wanted.jjsbd.lxpmvc.course.dto;

public record CourseSearchRequest(String q) {
	/// 검색어(q)가 null로 들어오면 빈 문자열("")로 바꿔서 NullPointerException을 방지.
	public CourseSearchRequest {
		q = q == null ? "" : q;
	}
}
