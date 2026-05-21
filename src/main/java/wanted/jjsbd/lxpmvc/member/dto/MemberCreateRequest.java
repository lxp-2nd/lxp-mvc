package wanted.jjsbd.lxpmvc.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
	String password
) {
}
