package wanted.jjsbd.lxpmvc.cart.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wanted.jjsbd.lxpmvc.member.domain.Member;

@Entity
@Getter
@Table(name = "carts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false, unique = true)
	private Member member;

	@OneToMany(mappedBy = "cart")
	private List<CartItem> cartItems = new ArrayList<>();

	private Cart(Member member) {
		this.member = member;
	}

	public static Cart create(Member member) {
		return new Cart(member);
	}
}
