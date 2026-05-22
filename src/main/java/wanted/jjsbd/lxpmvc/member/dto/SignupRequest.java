package wanted.jjsbd.lxpmvc.member.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignupRequest(
	@NotBlank(message = "이메일을 입력해주세요.")
	@Email(message = "올바른 이메일 형식으로 입력해주세요.")
	@Size(max = 100, message = "이메일은 최대 100자까지 입력할 수 있습니다.")
	String email,

	@NotBlank(message = "비밀번호를 입력해주세요.")
	@Size(min = 4, max = 20, message = "비밀번호는 4자 이상 20자 이하여야 합니다.")
	@Pattern(
		regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{4,20}$",
		message = "비밀번호는 영문, 숫자, 특수문자를 모두 포함해야 합니다."
	)
	String password,

	@NotBlank(message = "비밀번호 확인을 입력해주세요.")
	String passwordConfirm,

	@NotBlank(message = "닉네임을 입력해주세요.")
	@Size(min = 2, max = 20, message = "닉네임은 2자 이상 20자 이하여야 합니다.")
	String nickname
) {

	public static SignupRequest empty() {
		return new SignupRequest("", "", "", "");
	}

	@AssertTrue(message = "올바른 이메일 형식으로 입력해주세요.")
	public boolean isEmailWithoutWhitespace() {
		return email == null || email.equals(email.trim());
	}

	@AssertTrue(message = "비밀번호 확인이 일치하지 않습니다.")
	public boolean isPasswordConfirmMatching() {
		return password != null && password.equals(passwordConfirm);
	}

	public MemberCreateRequest toMemberCreateRequest() {
		return new MemberCreateRequest(nickname, email, password);
	}
}
