package wanted.jjsbd.lxpmvc.cart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import wanted.jjsbd.lxpmvc.cart.domain.Cart;
import wanted.jjsbd.lxpmvc.cart.domain.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

	List<CartItem> findByCartOrderByCreatedAtDesc(Cart cart);
}