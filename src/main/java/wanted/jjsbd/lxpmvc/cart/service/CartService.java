package wanted.jjsbd.lxpmvc.cart.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import wanted.jjsbd.lxpmvc.member.repository.MemberRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartService {

	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final CourseRepository courseRepository;
	private final MemberRepository memberRepository;

	// 로그인한 회원의 장바구니를 조회한다.
	// 장바구니가 아직 생성되지 않은 회원은 빈 장바구니 응답을 반환한다.
	public CartResponse getCart(Long memberId) {
		return cartRepository.findByMemberId(memberId)
			.map(this::toCartResponse)
			.orElseGet(CartResponse::empty);
	}

	// 로그인한 회원의 장바구니에 강의를 담는다.
	// 이미 삭제 처리된 동일 강의 항목이 있으면 새로 생성하지 않고 복구한다.
	@Transactional
	public void addCartItem(Long memberId, Long courseId) {
		Member member = findMember(memberId);
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

	// 로그인한 회원의 장바구니 항목을 선택 삭제한다.
	// 요청 항목은 회원 소유 범위와 삭제되지 않은 항목으로 제한하여 조회한다.
	@Transactional
	public void deleteCartItems(Long memberId, List<Long> cartItemIds) {

		// 삭제할 장바구니 항목을 선택하지 않은 경우
		if (cartItemIds == null || cartItemIds.isEmpty()) {
			throw new CustomException(ErrorCode.CART_ITEM_SELECTION_REQUIRED);
		}

		// 같은 cartItemId가 중복으로 넘어올 수 있으므로 중복 제거한다.
		List<Long> uniqueCartItemIds = cartItemIds.stream()
			.distinct()
			.toList();

		// 삭제 요청한 cartItemId 중 로그인한 회원의 삭제 가능한 장바구니 항목만 조회한다.
		List<CartItem> cartItems = cartItemRepository.findDeletableCartItems(memberId, uniqueCartItemIds);

		// 삭제 요청한 항목이 모두 조회되지 않으면 존재하지 않거나, 이미 삭제됐거나, 본인 항목이 아닌 경우로 판단한다.
		if (cartItems.size() != uniqueCartItemIds.size()) {
			throw new CustomException(ErrorCode.CART_ITEM_NOT_FOUND);
		}

		// 장바구니 항목 소프트 삭제
		cartItems.forEach(CartItem::delete);
	}

	// 로그인 정보에서 전달된 회원 id가 실제 회원인지 확인한다.
	private Member findMember(Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
	}

	// 회원의 장바구니를 조회하고, 없으면 새로 생성한다.
	private Cart findOrCreateCart(Long memberId, Member member) {
		return cartRepository.findByMemberId(memberId)
			.orElseGet(() -> createCartSafely(memberId, member));
	}

	// 동시에 같은 회원의 장바구니 생성 요청이 들어올 수 있으므로,
	// unique 제약 조건 충돌 시 이미 생성된 장바구니를 다시 조회한다.
	private Cart createCartSafely(Long memberId, Member member) {
		try {
			return cartRepository.saveAndFlush(Cart.create(member));
		} catch (DataIntegrityViolationException e) {
			return cartRepository.findByMemberId(memberId)
				.orElseThrow(() -> e);
		}
	}

	// 장바구니에는 존재하는 강의만 담을 수 있다.
	private Course findCourse(Long courseId) {
		return courseRepository.findById(courseId)
			.orElseThrow(() -> new CustomException(ErrorCode.COURSE_NOT_FOUND));
	}

	// 삭제되지 않은 장바구니 항목만 담은 최신순으로 조회하여 응답 DTO로 변환한다.
	private CartResponse toCartResponse(Cart cart) {
		List<CartItemResponse> cartItems = cartItemRepository.findByCartAndDeletedAtIsNullOrderByModifiedAtDesc(cart)
			.stream()
			.map(CartItemResponse::from)
			.toList();

		return CartResponse.from(cartItems);
	}

	/**
	 * min 추가
	 * 수강 신청 완료 후, 신청한 강의(courseId)들을 장바구니에서 찾아 소프트 삭제한다.
	 */
	@Transactional
	public void deleteCartItemsByCourseIds(Long memberId, List<Long> courseIds) {
		if (courseIds == null || courseIds.isEmpty()) {
			return;
		}

		// 중복 courseId 제거
		List<Long> uniqueCourseIds = courseIds.stream()
			.distinct()
			.toList();

		// 업데이트된 레포지토리 메서드 호출 (객체 탐색을 통한 조회)
		List<CartItem> cartItems = cartItemRepository.findCartItemsByCourseIds(memberId, uniqueCourseIds);

		// 장바구니 항목 소프트 삭제 (BaseEntity 또는 CartItem에 정의된 delete() 메서드 호출)
		cartItems.forEach(CartItem::delete);
	}

}

