package wanted.jjsbd.lxpmvc.cart.dto;

public record CartItemResponse(String cartItemId, String courseId, String courseTitle, String instructor,
		boolean selected) {
}
