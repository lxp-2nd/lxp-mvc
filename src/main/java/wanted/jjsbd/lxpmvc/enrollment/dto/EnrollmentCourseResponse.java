package wanted.jjsbd.lxpmvc.enrollment.dto;

import java.time.format.DateTimeFormatter;

import wanted.jjsbd.lxpmvc.enrollment.domain.Enrollment;

public record EnrollmentCourseResponse(
	String enrollmentId,
	String courseId,
	String courseTitle,
	String instructor,
	String createdAt
) {

	// Entity To DTO로 변환 편의 메서드
	public static EnrollmentCourseResponse from(Enrollment enrollment) {
		// 날짜 포맷팅 (BaseEntity에 getCreatedAt()이 있다고 가정)
		String formattedDate = enrollment.getCreatedAt() != null
			? enrollment.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
			: "";

		return new EnrollmentCourseResponse(
			String.valueOf(enrollment.getId()),
			String.valueOf(enrollment.getCourse().getId()),
			enrollment.getCourse().getTitle(),        // Course 엔티티의 실제 필드명에 맞게 수정 필요
			enrollment.getCourse().getInstructor().getNickname(),   // Course 엔티티의 실제 필드명에 맞게 수정 필요
			formattedDate
		);
	}

}
