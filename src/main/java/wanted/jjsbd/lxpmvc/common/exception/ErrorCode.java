package wanted.jjsbd.lxpmvc.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 1. Common - 기본 및 시스템 오류
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "COMMON_000", "잘못된 입력값입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_001", "서버 내부 오류가 발생했습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "COMMON_002", "지원하지 않는 HTTP 메서드입니다."),

    // 2. Common - Validation
    REQUIRED_VALUE_MISSING(HttpStatus.BAD_REQUEST, "COMMON_003", "필수값을 입력 또는 선택해주세요."),
    INVALID_NUMBER_RANGE(HttpStatus.BAD_REQUEST, "COMMON_004", "유효하지 않은 숫자 범위입니다. (1 이상의 값이어야 합니다.)"),

    // 3. Common - Resource
    EMPTY_LIST(HttpStatus.NOT_FOUND, "COMMON_007", "조회할 내역이 없습니다."),

	// Course

	// Enrollment

	// Cart
	;

    private final HttpStatus httpStatus;
	private final String code;
    private final String message;
}
