package wanted.jjsbd.lxpmvc.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record MemberCreateRequest(
	@NotBlank(message = "닉네임은 필수 입력 항목입니다.")
	@Size(min = 2, max = 20, message = "이름은 2자 이상 20자 이하여야 합니다.")
	String nickname,

	@NotBlank(message = "이메일은 필수 입력 항목입니다.")
	@Size(max = 100, message = "이메일은 최대 100자까지 가능합니다.")
	@Email(message = "올바르지 않은 이메일 형식입니다.")
	String email,

	@NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
	@Size(min = 4, max = 20, message = "비밀번호는 4자 이상 20자 이하여야 합니다.")
	@Pattern(
		regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{4,20}$",
		message = "비밀번호는 영문, 숫자, 특수문자를 모두 포함해야 합니다."
	)
	String password
) {
	public String getNormalizedEmail() {
		return email == null ? null : email.trim().toLowerCase();
	}
}
