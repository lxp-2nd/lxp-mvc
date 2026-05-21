package wanted.jjsbd.lxpmvc.cart.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import wanted.jjsbd.lxpmvc.cart.dto.CartResponse;
import wanted.jjsbd.lxpmvc.cart.service.CartService;
import wanted.jjsbd.lxpmvc.member.domain.AuthInfo;

@Controller
public class CartController {

	private static final String CART_TITLE = "장바구니";

	private final CartService cartService;

	public CartController(CartService cartService) {
		this.cartService = cartService;
	}

	// 로그인한 회원에 한하여 장바구니 화면 조회
	@GetMapping("/cart")
	public String cart(@AuthenticationPrincipal AuthInfo authInfo, Model model) {
		CartResponse cart = cartService.getCart(authInfo.memberId());

		model.addAttribute("title", CART_TITLE);
		model.addAttribute("cart", cart);
		model.addAttribute("cartItems", cart.cartItems());

		return "cart/index";

	}

	// 강의 상세 화면에서 장바구니 담기
	@PostMapping("/courses/{courseId}/cart")
	public String addCartItem(@AuthenticationPrincipal AuthInfo authInfo,
		@PathVariable Long courseId) {
		cartService.addCartItem(authInfo.memberId(), courseId);

		return "redirect:/cart";
	}
}


