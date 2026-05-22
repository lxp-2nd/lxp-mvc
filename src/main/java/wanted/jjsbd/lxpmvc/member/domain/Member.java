package wanted.jjsbd.lxpmvc.member.domain;

import java.util.regex.Pattern;

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
import wanted.jjsbd.lxpmvc.common.domain.DomainValidator;
import wanted.jjsbd.lxpmvc.common.exception.CustomException;
import wanted.jjsbd.lxpmvc.common.exception.ErrorCode;

@Entity
@Table(name = "members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
	private static final int MIN_NICKNAME_LENGTH = 2;
	private static final int MAX_NICKNAME_LENGTH = 20;
	private static final int MAX_EMAIL_LENGTH = 100;
	private static final int MAX_PASSWORD_HASH_LENGTH = 200;
	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	@Column(nullable = false, length = MAX_NICKNAME_LENGTH)
	private String nickname;

	@Column(nullable = false, unique = true, length = MAX_EMAIL_LENGTH)
	private String email;

	@Column(name = "password_hash", nullable = false, length = MAX_PASSWORD_HASH_LENGTH)
	private String passwordHash;

	@Column(name = "profile_img")
	private String profileImg;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private MemberRole role;

	private Member(String nickname, String email, String passwordHash, MemberRole role, String profileImg) {
		validateNickname(nickname);
		validateEmail(email);
		validatePasswordHash(passwordHash);
		validateRole(role);
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
		DomainValidator.validateNotBlank(nickname);
		this.nickname = nickname;
		this.profileImg = profileImg;
	}

	private void validateNickname(String nickname) {
		DomainValidator.validateNotBlank(nickname);
		if (nickname.length() < MIN_NICKNAME_LENGTH || nickname.length() > MAX_NICKNAME_LENGTH) {
			throw new CustomException(ErrorCode.MEMBER_INVALID_NAME);
		}
	}

	private void validateEmail(String email) {
		DomainValidator.validateNotBlank(email);
		if (email.length() > MAX_EMAIL_LENGTH || !EMAIL_PATTERN.matcher(email).matches()) {
			throw new CustomException(ErrorCode.MEMBER_INVALID_EMAIL_FORMAT);
		}
	}

	private void validatePasswordHash(String passwordHash) {
		DomainValidator.validateNotBlank(passwordHash);
		if (passwordHash.length() > MAX_PASSWORD_HASH_LENGTH) {
			throw new CustomException(ErrorCode.MEMBER_INVALID_PASSWORD);
		}
	}

	private void validateRole(MemberRole role) {
		if (role == null) {
			throw new CustomException(ErrorCode.REQUIRED_VALUE_MISSING);
		}
	}
}
