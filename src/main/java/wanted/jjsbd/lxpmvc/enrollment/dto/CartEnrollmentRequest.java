package wanted.jjsbd.lxpmvc.enrollment.dto;

import java.util.List;

public record CartEnrollmentRequest(List<String> courseIds) {

	public CartEnrollmentRequest {
		courseIds = courseIds == null ? List.of() : List.copyOf(courseIds);
	}
}
