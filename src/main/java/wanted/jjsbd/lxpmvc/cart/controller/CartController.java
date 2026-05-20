package wanted.jjsbd.lxpmvc.cart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import wanted.jjsbd.lxpmvc.cart.dto.CartAddRequest;
import wanted.jjsbd.lxpmvc.cart.dto.CartResponse;
import wanted.jjsbd.lxpmvc.common.MockLxpData;

@Controller
public class CartController {

	private final MockLxpData mockData;

	public CartController(MockLxpData mockData) {
		this.mockData = mockData;
	}

	@GetMapping("/cart")
	public String cart(Model model) {
		CartResponse cart = mockData.cart();

		model.addAttribute("title", "장바구니");
		model.addAttribute("cart", cart);
		model.addAttribute("cartItems", cart.cartItems());
		model.addAttribute("cartCount", cart.cartCount());
		model.addAttribute("selectedCount", cart.selectedCount());
		return "cart/index";
	}

	@PostMapping("/courses/{courseId}/cart")
	public String addCart(@PathVariable String courseId) {
		CartAddRequest request = new CartAddRequest(courseId);
		return "redirect:/cart?courseId=" + request.courseId();
	}
}
