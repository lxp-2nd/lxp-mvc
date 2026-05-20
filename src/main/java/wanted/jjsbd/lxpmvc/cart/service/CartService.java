package wanted.jjsbd.lxpmvc.cart.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import wanted.jjsbd.lxpmvc.cart.domain.Cart;
import wanted.jjsbd.lxpmvc.cart.dto.CartItemResponse;
import wanted.jjsbd.lxpmvc.cart.dto.CartResponse;
import wanted.jjsbd.lxpmvc.cart.repository.CartItemRepository;
import wanted.jjsbd.lxpmvc.cart.repository.CartRepository;
import wanted.jjsbd.lxpmvc.member.domain.Member;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartService {

	// 썸네일 공통으로 둘건지 팀원과 상의 후 수정 예정
	private static final String DEFAULT_THUMBNAIL_URL = "/images/default-course.png";
	// Member 도메인 머지 되면 수정 예정
	private static final String DEFAULT_INSTRUCTOR_NAME = "강사명 미정";
	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;

	// 로그인한 회원의 장바구니를 조회 메서드
	public CartResponse getCart(Member loginMember) {
		return cartRepository.findByMember(loginMember)
			.map(this::toCartResponse)
			.orElseGet(CartResponse::empty);
	}

	// 담은 일시 최신순으로 조회 메서드
	private CartResponse toCartResponse(Cart cart) {
		List<CartItemResponse> cartItems = cartItemRepository.findByCartOrderByCreatedAtDesc(cart)
			.stream()
			.map(CartItemResponse::from)
			.toList();

		return CartResponse.from(cartItems);
	}

}
