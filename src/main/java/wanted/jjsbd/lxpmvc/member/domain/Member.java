package wanted.jjsbd.lxpmvc.member.domain;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wanted.jjsbd.lxpmvc.common.domain.BaseEntity;
import wanted.jjsbd.lxpmvc.common.exception.CustomException;
import wanted.jjsbd.lxpmvc.common.exception.ErrorCode;

@Entity
@Table(name = "members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	@Column(nullable = false, length = 20)
	private String nickname;

	@Column(nullable = false, unique = true, length = 100)
	private String email;

	@Column(name = "password_hash", nullable = false, length = 200)
	private String passwordHash;

	@Column(name = "profile_img")
	private String profileImg;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private MemberRole role;

	private Member(String nickname, String email, String passwordHash, MemberRole role, String profileImg) {
		this.nickname = nickname;
		this.email = email;
		this.passwordHash = passwordHash;
		this.role = role;
		this.profileImg = profileImg;
	}

	public static Member createBasicMember(String nickname, String email, String passwordHash) {
		return new Member(nickname, email, passwordHash, MemberRole.BASIC, null);
	}

	public void updateProfile(String nickname, String profileImg) {
		if (nickname != null && !nickname.isBlank()) {
			this.nickname = nickname;
		}
		this.profileImg = profileImg;
	}
}
