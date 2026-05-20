package wanted.jjsbd.lxpmvc.cart.dto;

import java.util.List;

public record CartResponse(
	List<CartItemResponse> cartItems
) {

	public static CartResponse from(List<CartItemResponse> cartItems) {
		return new CartResponse(cartItems);
	}

	public static CartResponse empty() {
		return new CartResponse(List.of());
	}
}