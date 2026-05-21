package wanted.jjsbd.lxpmvc.cart.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import wanted.jjsbd.lxpmvc.cart.domain.Cart;
import wanted.jjsbd.lxpmvc.cart.domain.CartItem;
import wanted.jjsbd.lxpmvc.cart.dto.CartItemResponse;
import wanted.jjsbd.lxpmvc.cart.dto.CartResponse;
import wanted.jjsbd.lxpmvc.cart.repository.CartItemRepository;
import wanted.jjsbd.lxpmvc.cart.repository.CartRepository;
import wanted.jjsbd.lxpmvc.course.domain.Course;
import wanted.jjsbd.lxpmvc.course.repository.CourseRepository;
import wanted.jjsbd.lxpmvc.member.domain.Member;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartService {

	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final CourseRepository courseRepository;
	private final EntityManager entityManager;

	// 로그인한 회원의 장바구니 조회
	public CartResponse getCart(Long memberId) {
		return cartRepository.findByMember_Id(memberId)
			.map(this::toCartResponse)
			.orElseGet(CartResponse::empty);
	}

	// 강의 장바구니 담기
	@Transactional
	public void addCartItem(Long memberId, Long courseId) {
		Member member = entityManager.getReference(Member.class, memberId);

		Cart cart = cartRepository.findByMember_Id(memberId)
			.orElseGet(() -> cartRepository.save(Cart.create(member)));

		Course course = courseRepository.findById(courseId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 강의입니다. courseId=" + courseId));

		if (cartItemRepository.existsByCartAndCourse(cart, course)) {
			return;
		}

		CartItem cartItem = CartItem.create(cart, course);
		cartItemRepository.save(cartItem);
	}

	// 담은 일시 최신순으로 조회
	private CartResponse toCartResponse(Cart cart) {
		List<CartItemResponse> cartItems = cartItemRepository.findByCartOrderByCreatedAtDesc(cart)
			.stream()
			.map(CartItemResponse::from)
			.toList();

		return CartResponse.from(cartItems);
	}
}






