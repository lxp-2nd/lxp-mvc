package wanted.jjsbd.lxpmvc.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MemberProfileRequest(
	@NotBlank(message = "닉네임을 입력해주세요.")
	@Size(min = 2, max = 20, message = "닉네임은 2자 이상 20자 이하여야 합니다.")
	String nickname
) {
}
