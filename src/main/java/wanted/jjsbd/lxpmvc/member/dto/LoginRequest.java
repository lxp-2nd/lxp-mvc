package wanted.jjsbd.lxpmvc.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoginRequest {

	@NotBlank(message = "MEMBER_REQUIRED_EMAIL")
	@Email(message = "MEMBER_INVALID_EMAIL_FORMAT")
	private String username;

	@NotBlank(message = "MEMBER_REQUIRED_PASSWORD")
	private String password;
}