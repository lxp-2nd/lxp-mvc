package wanted.jjsbd.lxpmvc.cart.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import wanted.jjsbd.lxpmvc.cart.domain.Cart;
import wanted.jjsbd.lxpmvc.member.domain.Member;

public interface CartRepository extends JpaRepository<Cart, Long> {

	Optional<Cart> findByMember(Member member);
}