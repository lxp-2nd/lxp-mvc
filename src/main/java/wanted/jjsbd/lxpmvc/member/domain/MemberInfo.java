package wanted.jjsbd.lxpmvc.member.domain;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public record MemberInfo(
	Long memberId,
	String email,
	String nickname,
	MemberRole role
) {

	public static MemberInfo from(Member member) {
		return new MemberInfo(
			member.getId(),
			member.getEmail(),
			member.getNickname(),
			member.getRole()
		);
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		String roleName = role.name().startsWith("ROLE_") ? role.name() : "ROLE_" + role.name();
		return List.of(new SimpleGrantedAuthority(roleName));
	}

	public String getEmail() {
		return email;
	}
}