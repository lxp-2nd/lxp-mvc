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
import wanted.jjsbd.lxpmvc.common.exception.CustomException;
import wanted.jjsbd.lxpmvc.common.exception.ErrorCode;
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
		Cart cart = findOrCreateCart(memberId, member);
		Course course = findCourse(courseId);

		cartItemRepository.findByCartAndCourse(cart, course)
			.ifPresentOrElse(
				cartItem -> {
					if (cartItem.isDeleted()) {
						cartItem.restore();
					}
				},
				() -> cartItemRepository.save(CartItem.create(cart, course))
			);
	}

	private Cart findOrCreateCart(Long memberId, Member member) {
		return cartRepository.findByMember_Id(memberId)
			.orElseGet(() -> cartRepository.save(Cart.create(member)));
	}

	private Course findCourse(Long courseId) {
		return courseRepository.findById(courseId)
			.orElseThrow(() -> new CustomException(ErrorCode.COURSE_NOT_FOUND));
	}

	private CartResponse toCartResponse(Cart cart) {
		List<CartItemResponse> cartItems = cartItemRepository.findByCartAndDeletedAtIsNullOrderByCreatedAtDesc(cart)
			.stream()
			.map(CartItemResponse::from)
			.toList();

		return CartResponse.from(cartItems);
	}

	@Transactional
	public void deleteCartItems(Long memberId, List<Long> cartItemIds) {

		// 삭제할 장바구니 항목을 선택하지 않은 경우
		if (cartItemIds == null || cartItemIds.isEmpty()) {
			throw new CustomException(ErrorCode.CART_ITEM_SELECTION_REQUIRED);
		}

		// 같은 cartItemId가 중복으로 넘어올 수 있으므로 중복 제거
		List<Long> uniqueCartItemIds = cartItemIds.stream()
			.distinct()
			.toList();

		// 삭제되지 않은 장바구니 항목만 조회
		List<CartItem> cartItems = cartItemRepository.findAllByCartItemIdInAndDeletedAtIsNull(uniqueCartItemIds);

		// 요청한 장바구니 항목 중 존재하지 않거나 이미 삭제된 항목이 있는 경우
		if (cartItems.size() != uniqueCartItemIds.size()) {
			throw new CustomException(ErrorCode.CART_DELETE_ACCESS_DENIED);
		}

		// 선택한 항목 중 로그인한 회원의 장바구니 항목이 아닌 것이 있는지 확인
		boolean hasOtherMemberCartItem = cartItems.stream()
			.anyMatch(cartItem -> !cartItem.getCart().getMember().getId().equals(memberId));

		// 본인의 장바구니 항목이 아닌 경우 삭제 불가
		if (hasOtherMemberCartItem) {
			throw new CustomException(ErrorCode.CART_DELETE_ACCESS_DENIED);
		}

		cartItems.forEach(CartItem::delete);
	}
}













