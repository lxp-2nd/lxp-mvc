package wanted.jjsbd.lxpmvc.enrollment.dto;

public record EnrollmentCourseResponse(
	String enrollmentId,
	String courseId,
	String courseTitle,
	String instructor,
	String statusText
) {
}
