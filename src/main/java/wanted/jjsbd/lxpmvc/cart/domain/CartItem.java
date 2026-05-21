package wanted.jjsbd.lxpmvc.cart.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wanted.jjsbd.lxpmvc.common.domain.BaseEntity;
import wanted.jjsbd.lxpmvc.course.domain.Course;

@Entity
@Getter
@Table(name = "cart_items", uniqueConstraints = {
	@UniqueConstraint(name = "uk_cart_course", columnNames = {"cart_id", "course_id"})})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItem extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cartItemId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cart_id", nullable = false)
	private Cart cart;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "course_id", nullable = false)
	private Course course;

	private CartItem(Cart cart, Course course) {
		this.cart = cart;
		this.course = course;
	}

	public static CartItem create(Cart cart, Course course) {
		return new CartItem(cart, course);
	}

}
