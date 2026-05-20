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

	// Course

	// Enrollment
	ENROLLMENT_SELECTION_REQUIRED(HttpStatus.BAD_REQUEST, "ENROLL_001", "신청할 강의를 선택해주세요."),
	ENROLLMENT_ALREADY_EXISTS(HttpStatus.CONFLICT, "ENROLL_002", "이미 신청한 강의입니다."),
	ENROLLMENT_ALREADY_EXISTS_SKIPPED(HttpStatus.CONFLICT, "ENROLL_003", "이미 신청한 강의는 신청 처리되지 않았습니다."),
	ENROLLMENT_PARTIAL_CONFLICT(HttpStatus.CONFLICT, "ENROLL_004", "일부 강의만 신청되었습니다. 신청할 수 없는 강의는 장바구니에 남아 있습니다."),
	ENROLLMENT_CANCEL_ACCESS_DENIED(HttpStatus.FORBIDDEN, "ENROLL_005", "본인이 신청한 강의만 취소할 수 있습니다."),
	ENROLLMENT_ALREADY_CANCELLED(HttpStatus.CONFLICT, "ENROLL_006", "이미 취소된 신청입니다."),
	ENROLLMENT_EMPTY(HttpStatus.NOT_FOUND, "ENROLL_007", "신청 완료한 강의가 없습니다."),
	ENROLLMENT_LIST_ACCESS_DENIED(HttpStatus.FORBIDDEN, "ENROLL_008", "본인의 수강 목록만 조회할 수 있습니다."),
	ENROLLMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "ENROLL_009", "신청 정보를 찾을 수 없습니다.")

	// Cart
	;

    private final HttpStatus httpStatus;
	private final String code;
    private final String message;
}
