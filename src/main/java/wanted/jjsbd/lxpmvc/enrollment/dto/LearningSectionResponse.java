package wanted.jjsbd.lxpmvc.enrollment.dto;

import java.util.List;

public record LearningSectionResponse(String title, List<LearningCourseMaterialResponse> courseMaterials) {
}
