package wanted.jjsbd.lxpmvc.enrollment.dto;

import java.util.List;

public record LearningResponse(String enrollmentId, String courseId, String courseTitle,
		List<LearningSectionResponse> sections, LearningCourseMaterialResponse selectedCourseMaterial) {
}
