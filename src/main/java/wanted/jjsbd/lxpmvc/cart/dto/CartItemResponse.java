package wanted.jjsbd.lxpmvc.cart.dto;

import java.time.LocalDateTime;

import wanted.jjsbd.lxpmvc.cart.domain.CartItem;
import wanted.jjsbd.lxpmvc.course.domain.Course;
import wanted.jjsbd.lxpmvc.course.domain.CourseInstructor;

public record CartItemResponse(
	Long cartItemId,
	Long courseId,
	String courseTitle,
	String instructor,
	String courseDescription,
	String thumbnailUrl,
	LocalDateTime modifiedAt
) {

	public static CartItemResponse from(CartItem cartItem) {
		Course course = cartItem.getCourse();
		CourseInstructor instructor = course.getInstructorInfo();

		return new CartItemResponse(
			cartItem.getCartItemId(),
			course.getId(),
			course.getTitle(),
			instructor.getName(),
			course.getDescription(),
			course.getThumbnailUrl(),
			cartItem.getModifiedAt()
		);
	}
}
