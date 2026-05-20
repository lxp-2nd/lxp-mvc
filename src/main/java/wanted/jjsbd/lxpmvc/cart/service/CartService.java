package wanted.jjsbd.lxpmvc.cart.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wanted.jjsbd.lxpmvc.cart.domain.Cart;
import wanted.jjsbd.lxpmvc.cart.domain.CartItem;
import wanted.jjsbd.lxpmvc.cart.dto.CartItemResponse;
import wanted.jjsbd.lxpmvc.cart.dto.CartResponse;
import wanted.jjsbd.lxpmvc.cart.repository.CartItemRepository;
import wanted.jjsbd.lxpmvc.cart.repository.CartRepository;
import wanted.jjsbd.lxpmvc.course.domain.Course;
import wanted.jjsbd.lxpmvc.member.domain.Member;

@Service
@Transactional(readOnly = true)
public class CartService {

	// 확인해야됨. 썸네일은 디폴트 이미지로 하기로 했는데 공통으로 뺄건지.
	private static final String DEFAULT_THUMBNAIL_URL = "/images/default-course.png";
	private static final String DEFAULT_INSTRUCTOR_NAME = "강사명 미정";

	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;

	// 어노테이션으로 가능한지 확인.
	public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository) {
		this.cartRepository = cartRepository;
		this.cartItemRepository = cartItemRepository;
	}

	// 로그인한 회원의 장바구니를 조회한다.
	public CartResponse getCart(Member loginMember) {
		return cartRepository.findByMember(loginMember)
			.map(this::toCartResponse)
			.orElseGet(CartResponse::empty);
	}

	//	CartResponse 에서 내부에서 정적 메서드로 만드는게 깔끔해서 리팩토링 고려해봄
	private CartResponse toCartResponse(Cart cart) {
		List<CartItemResponse> cartItems = cartItemRepository.findByCartOrderByCreatedAtDesc(cart)
			.stream()
			.map(this::toCartItemResponse)
			.toList();

		return new CartResponse(cartItems);
	}

	//	CartResponse 에서 내부에서 정적 메서드로 만드는게 깔끔해서 리팩토링 고려해봄
	private CartItemResponse toCartItemResponse(CartItem cartItem) {
		Course course = cartItem.getCourse();

		return new CartItemResponse(
			cartItem.getCartItemId(),
			course.getId(),
			course.getTitle(),
			DEFAULT_INSTRUCTOR_NAME,
			course.getDescription(),
			DEFAULT_THUMBNAIL_URL,
			cartItem.getCreatedAt()
		);
	}
}


