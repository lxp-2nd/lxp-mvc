package wanted.jjsbd.lxpmvc.member.dto;

public record LoginRequest(String email, String password) {

	public LoginRequest {
		email = email == null ? "" : email;
		password = password == null ? "" : password;
	}
}
