package wanted.jjsbd.lxpmvc.member.dto;

public record SignupRequest(String email, String password, String name) {

	public SignupRequest {
		email = email == null ? "" : email;
		password = password == null ? "" : password;
		name = name == null ? "" : name;
	}
}
