package wanted.jjsbd.lxpmvc.cart.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wanted.jjsbd.lxpmvc.course.domain.Course;

@Entity
@Getter
@Table(
	name = "cart_items",
	uniqueConstraints = {
		@UniqueConstraint(
			name = "uk_cart_course",
			columnNames = {"cart_id", "course_id"}
		)
	}
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cartItemId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cart_id", nullable = false)
	private Cart cart;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "course_id", nullable = false)
	private Course course;

	@Column(name = "added_at", nullable = false, updatable = false)
	private LocalDateTime addedAt;

	// 팀 공통 BaseTimeEntity 쓰기로 확정되면 createdAt 으로 변수명 변경 예정
	@PrePersist
	private void prePersist() {
		if (addedAt == null) {
			addedAt = LocalDateTime.now();
		}
	}
}