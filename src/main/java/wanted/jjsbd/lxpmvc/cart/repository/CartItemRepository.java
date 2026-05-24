package wanted.jjsbd.lxpmvc.cart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import wanted.jjsbd.lxpmvc.cart.domain.Cart;
import wanted.jjsbd.lxpmvc.cart.domain.CartItem;
import wanted.jjsbd.lxpmvc.course.domain.Course;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

	// 장바구니 조회. 삭제되지 않은 항목만 담은 최신순으로 조회한다.
	List<CartItem> findByCartAndDeletedAtIsNullOrderByModifiedAtDesc(Cart cart);

	// 장바구니 담기. 삭제된 항목도 복구 대상이므로 삭제 여부와 관계없이 조회한다.
	Optional<CartItem> findByCartAndCourse(Cart cart, Course course);

	// 장바구니 삭제. 삭제 요청한 항목 중 로그인한 회원의 삭제 가능한 항목만 조회한다.
	@Query("""
		select ci
		from CartItem ci
		where ci.cart.member.id = :memberId
		  and ci.cartItemId in :cartItemIds
		  and ci.deletedAt is null
		""")
	List<CartItem> findDeletableCartItems(
		@Param("memberId") Long memberId,
		@Param("cartItemIds") List<Long> cartItemIds
	);
}

