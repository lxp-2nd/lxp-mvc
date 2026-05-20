package wanted.jjsbd.lxpmvc.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
	@NotBlank(message = "MEMBER_REQUIRED_EMAIL")
	@Email(message = "MEMBER_INVALID_EMAIL_FORMAT")
	String email,

	@NotBlank(message = "MEMBER_REQUIRED_PASSWORD")
	String password
) {
}