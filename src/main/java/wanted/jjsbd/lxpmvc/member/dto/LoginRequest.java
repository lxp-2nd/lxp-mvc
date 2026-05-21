package wanted.jjsbd.lxpmvc.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
	@NotBlank(message = "이메일을 입력해주세요.")
	@Email(message = "올바르지 않은 이메일 형식입니다.")
	String email,

	@NotBlank(message = "비밀번호를 입력해주세요.")
	String password
) {
	public static LoginRequest empty() {
		return new LoginRequest("", "");
	}

	public String getNormalizedEmail() {
		return email == null ? null : email.trim().toLowerCase();
	}
}
