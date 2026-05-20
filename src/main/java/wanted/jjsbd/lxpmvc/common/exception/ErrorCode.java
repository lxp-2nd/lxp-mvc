package wanted.jjsbd.lxpmvc.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	// Common
	INVALID_INPUT(HttpStatus.BAD_REQUEST, "COMMON_000", "잘못된 입력값입니다."),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_001", "서버 내부 오류가 발생했습니다."),

	// Member
	MEMBER_REQUIRED_EMAIL(HttpStatus.BAD_REQUEST, "MEMBER_001", "이메일을 입력해주세요."),
	MEMBER_REQUIRED_PASSWORD(HttpStatus.BAD_REQUEST, "MEMBER_002", "비밀번호를 입력해주세요."),
	MEMBER_REQUIRED_NAME(HttpStatus.BAD_REQUEST, "MEMBER_003", "이름을 입력해주세요."),
	MEMBER_INVALID_EMAIL_FORMAT(HttpStatus.BAD_REQUEST, "MEMBER_004", "올바르지 않은 이메일 형식입니다."),
	MEMBER_DUPLICATE_EMAIL(HttpStatus.CONFLICT, "MEMBER_005", "이미 사용 중인 이메일입니다."),
	MEMBER_INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "MEMBER_006", "비밀번호는 4자 이상 20자 이하여야 합니다."),
	MEMBER_PASSWORD_CONFIRM_MISMATCH(HttpStatus.BAD_REQUEST, "MEMBER_007", "비밀번호 확인이 일치하지 않습니다."),
	MEMBER_INVALID_NAME(HttpStatus.BAD_REQUEST, "MEMBER_008", "이름은 2자 이상 20자 이하여야 합니다."),
	MEMBER_INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "MEMBER_009", "이메일 또는 비밀번호가 올바르지 않습니다."),
	MEMBER_LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED, "MEMBER_010", "로그인이 필요한 기능입니다."),
	MEMBER_PROFILE_ACCESS_DENIED(HttpStatus.FORBIDDEN, "MEMBER_011", "본인 정보만 조회할 수 있습니다."),
	MEMBER_EMAIL_NOT_EDITABLE(HttpStatus.BAD_REQUEST, "MEMBER_012", "이메일은 수정할 수 없습니다."),
	MEMBER_PROFILE_IMAGE_TOO_LARGE(HttpStatus.BAD_REQUEST, "MEMBER_013", "프로필 이미지는 5MB 이하로 업로드해주세요."),
	MEMBER_ALREADY_WITHDRAWN(HttpStatus.CONFLICT, "MEMBER_014", "이미 탈퇴 처리된 계정입니다."),
	MEMBER_WITHDRAW_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "MEMBER_015", "회원 탈퇴를 처리할 수 없습니다. 잠시 후 다시 시도해주세요."),

	// Course

	// Enrollment

	// Cart
	;

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;
}
