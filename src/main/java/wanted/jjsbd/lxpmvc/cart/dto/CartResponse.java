package wanted.jjsbd.lxpmvc.cart.dto;

import java.util.List;

public record CartResponse(List<CartItemResponse> cartItems, int cartCount, int selectedCount) {
}
