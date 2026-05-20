package wanted.jjsbd.lxpmvc.member.dto;

public record SignupRequest(String email, String password, String passwordConfirm, String name) {

	public SignupRequest {
		email = email == null ? "" : email;
		password = password == null ? "" : password;
		passwordConfirm = passwordConfirm == null ? "" : passwordConfirm;
		name = name == null ? "" : name;
	}
}
