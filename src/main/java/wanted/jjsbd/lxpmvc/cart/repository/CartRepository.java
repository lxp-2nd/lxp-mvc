package wanted.jjsbd.lxpmvc.cart.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import wanted.jjsbd.lxpmvc.cart.domain.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {

	@Query("select c from Cart c where c.member.id = :memberId")
	Optional<Cart> findByMemberId(@Param("memberId") Long memberId);
}

