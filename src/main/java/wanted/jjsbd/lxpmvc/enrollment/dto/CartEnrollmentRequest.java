package wanted.jjsbd.lxpmvc.enrollment.dto;

import java.util.List;

public record CartEnrollmentRequest(List<Long> courseIds) {

	public CartEnrollmentRequest {
		courseIds = courseIds == null ? List.of() : List.copyOf(courseIds);
	}

	public boolean isEmpty() {
		return courseIds == null || courseIds.isEmpty();
	}
}
