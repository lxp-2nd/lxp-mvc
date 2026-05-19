package wanted.jjsbd.lxpmvc.member.dto;

public record MemberProfileRequest(String name, String email) {

	public MemberProfileRequest {
		name = name == null ? "" : name;
		email = email == null ? "" : email;
	}
}
