package wanted.jjsbd.lxpmvc.cart.dto;

import java.time.LocalDateTime;

public record CartItemResponse(
	Long cartItemId,
	Long courseId,
	String courseTitle,
	String instructor,
	String courseDescription,
	String thumbnailUrl,
	LocalDateTime createdAt
) {
}
