package wanted.jjsbd.lxpmvc.cart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import wanted.jjsbd.lxpmvc.cart.dto.CartResponse;
import wanted.jjsbd.lxpmvc.cart.service.CartService;
import wanted.jjsbd.lxpmvc.member.domain.Member;

@Controller
public class CartController {

	private static final String CART_TITLE = "장바구니";
	private static final String LOGIN_REDIRECT = "redirect:/login";
	private static final String CART_VIEW = "cart/index";

	private final CartService cartService;

	public CartController(CartService cartService) {
		this.cartService = cartService;
	}

	// 로그인한 회원의 장바구니 화면을 조회한다.
	@GetMapping("/cart")
	public String cart(@SessionAttribute(name = "loginMember", required = false) Member loginMember, Model model) {
		if (loginMember == null) {
			return LOGIN_REDIRECT;
		}

		CartResponse cart = cartService.getCart(loginMember);

		model.addAttribute("title", CART_TITLE);
		model.addAttribute("cart", cart);
		model.addAttribute("cartItems", cart.cartItems());

		// cart/index 으로 하드코딩할건지 팀원이랑 협의 필요.
		return CART_VIEW;
	}
}

