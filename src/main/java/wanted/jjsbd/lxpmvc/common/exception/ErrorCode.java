package wanted.jjsbd.lxpmvc.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Common - 기본오류
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "COMMON_000", "잘못된 입력값입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_001", "서버 내부 오류가 발생했습니다."),

    // Common - Validation (null, 공백, 숫자 범위 등)
    REQUIRED_VALUE_MISSING(HttpStatus.BAD_REQUEST, "COMMON_002", "필수 값이 누락되었습니다."),
    VALUE_CANNOT_BE_BLANK(HttpStatus.BAD_REQUEST, "COMMON_003", "값이 비어있거나 공백일 수 없습니다."),
    INVALID_NUMBER_RANGE(HttpStatus.BAD_REQUEST, "COMMON_004", "유효하지 않은 숫자 범위입니다. (0 또는 양수여야 합니다.)"),

    // Common - 리소스 및 HTTP 상태 관련 자주 쓰는 에러
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON_005", "요청한 리소스를 찾을 수 없습니다."),
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "COMMON_006", "이미 존재하는 데이터입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "COMMON_007", "지원하지 않는 HTTP 메서드입니다."),
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "COMMON_008", "인증이 필요합니다. (로그인 필요)"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "COMMON_009", "해당 리소스에 대한 접근 권한이 없습니다."),
	// Member

	// Course

	// Enrollment

	// Cart
	;

    private final HttpStatus httpStatus;
	private final String code;
    private final String message;
}
