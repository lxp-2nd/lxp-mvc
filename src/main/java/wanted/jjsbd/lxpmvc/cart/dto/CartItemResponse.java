package wanted.jjsbd.lxpmvc.cart.dto;

import java.time.LocalDateTime;

import wanted.jjsbd.lxpmvc.cart.domain.CartItem;
import wanted.jjsbd.lxpmvc.course.domain.Course;
import wanted.jjsbd.lxpmvc.member.domain.Member;

public record CartItemResponse(
	Long cartItemId,
	Long courseId,
	String courseTitle,
	String instructor,
	String courseDescription,
	String thumbnailUrl,
	LocalDateTime createdAt
) {

	private static final String DEFAULT_THUMBNAIL_URL = "/images/default-course.png";

	public static CartItemResponse from(CartItem cartItem) {
		Course course = cartItem.getCourse();
		Member instructor = course.getInstructor();

		return new CartItemResponse(
			cartItem.getCartItemId(),
			course.getId(),
			course.getTitle(),
			instructor.getNickname(),
			course.getDescription(),
			DEFAULT_THUMBNAIL_URL,
			cartItem.getCreatedAt()
		);
	}
}



