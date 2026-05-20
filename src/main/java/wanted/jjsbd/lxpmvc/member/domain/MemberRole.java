package wanted.jjsbd.lxpmvc.member.domain;

import lombok.Getter;

@Getter
public enum MemberRole {
	BASIC("ROLE_BASIC"),
	INSTRUCTOR("ROLE_INSTRUCTOR"),
	ADMIN("ROLE_ADMIN");

	private final String securityCode;

	MemberRole(String securityCode) {
		this.securityCode = securityCode;
	}
}