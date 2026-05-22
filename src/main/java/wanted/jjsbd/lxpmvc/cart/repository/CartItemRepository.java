package wanted.jjsbd.lxpmvc.cart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import wanted.jjsbd.lxpmvc.cart.domain.Cart;
import wanted.jjsbd.lxpmvc.cart.domain.CartItem;
import wanted.jjsbd.lxpmvc.course.domain.Course;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

	List<CartItem> findByCartAndDeletedAtIsNullOrderByCreatedAtDesc(Cart cart);

	List<CartItem> findAllByCartItemIdInAndDeletedAtIsNull(List<Long> cartItemIds);

	Optional<CartItem> findByCartAndCourse(Cart cart, Course course);
}

















