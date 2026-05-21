package wanted.jjsbd.lxpmvc.cart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import wanted.jjsbd.lxpmvc.cart.domain.Cart;
import wanted.jjsbd.lxpmvc.cart.domain.CartItem;
import wanted.jjsbd.lxpmvc.course.domain.Course;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

	List<CartItem> findByCartAndDeletedAtIsNullOrderByCreatedAtDesc(Cart cart);

	boolean existsByCartAndCourseAndDeletedAtIsNull(Cart cart, Course course);

	List<CartItem> findAllByCartItemIdInAndDeletedAtIsNull(List<Long> cartItemIds);
}









