package wanted.jjsbd.lxpmvc.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	// 1. Common - 기본 및 시스템 오류
	INVALID_INPUT(HttpStatus.BAD_REQUEST, "COMMON_000", "잘못된 입력값입니다."), INTERNAL_SERVER_ERROR(
		HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_001", "서버 내부 오류가 발생했습니다."), METHOD_NOT_ALLOWED(
		HttpStatus.METHOD_NOT_ALLOWED, "COMMON_002", "지원하지 않는 HTTP 메서드입니다."),

	// 2. Common - Validation
	REQUIRED_VALUE_MISSING(HttpStatus.BAD_REQUEST, "COMMON_003", "필수값을 입력 또는 선택해주세요."), INVALID_NUMBER_RANGE(
		HttpStatus.BAD_REQUEST, "COMMON_004", "유효하지 않은 숫자 범위입니다. (1 이상의 값이어야 합니다.)"),

	// 3. Common - Resource
	EMPTY_LIST(HttpStatus.BAD_REQUEST, "COMMON_005", "목록이 비어 있습니다"),

	// Member
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
	MEMBER_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "MEMBER_016", "회원을 찾을 수 없습니다."),

	// Course
	COURSE_TITLE_REQUIRED(HttpStatus.BAD_REQUEST, "COURSE_001", "강의 제목은 필수/비어있을 수 없습니다."),
	COURSE_INSTRUCTOR_REQUIRED(HttpStatus.BAD_REQUEST, "COURSE_002", "강사 정보는 필수입니다."),
	COURSE_NOT_FOUND(HttpStatus.NOT_FOUND, "COURSE_003", "존재하지 않는 강의입니다."),

	SECTION_COURSE_REQUIRED(HttpStatus.BAD_REQUEST, "SECTION_001", "소속된 강의 정보는 필수입니다."),
	SECTION_TITLE_REQUIRED(HttpStatus.BAD_REQUEST, "SECTION_002", "섹션 제목은 필수/비어있을 수 없습니다."),
	SECTION_SEQUENCE_INVALID(HttpStatus.BAD_REQUEST, "SECTION_003", "섹션 순서는 1 이상의 유효한 값이어야 합니다."),

	MATERIAL_SECTION_REQUIRED(HttpStatus.BAD_REQUEST, "MATERIAL_001", "소속된 섹션 정보는 필수입니다."),
	MATERIAL_TITLE_REQUIRED(HttpStatus.BAD_REQUEST, "MATERIAL_002", "강의자료 제목은 필수/비어있을 수 없습니다."),
	MATERIAL_TYPE_REQUIRED(HttpStatus.BAD_REQUEST, "MATERIAL_003", "자료 유형(VIDEO/DOCUMENT)은 필수입니다."),
	MATERIAL_SEQUENCE_INVALID(HttpStatus.BAD_REQUEST, "MATERIAL_004", "자료 순서는 1 이상의 유효한 값이어야 합니다."),

	// Enrollment
	ENROLLMENT_SELECTION_REQUIRED(HttpStatus.BAD_REQUEST, "ENROLL_001", "신청할 강의를 선택해주세요."),
	ENROLLMENT_ALREADY_EXISTS(HttpStatus.CONFLICT, "ENROLL_002", "이미 신청한 강의입니다."),
	ENROLLMENT_ALREADY_EXISTS_SKIPPED(HttpStatus.CONFLICT, "ENROLL_003", "이미 신청한 강의는 신청 처리되지 않았습니다."),
	ENROLLMENT_PARTIAL_CONFLICT(HttpStatus.CONFLICT, "ENROLL_004", "일부 강의만 신청되었습니다. 신청할 수 없는 강의는 장바구니에 남아 있습니다."),
	ENROLLMENT_CANCEL_ACCESS_DENIED(HttpStatus.FORBIDDEN, "ENROLL_005", "본인이 신청한 강의만 취소할 수 있습니다."),
	ENROLLMENT_ALREADY_CANCELLED(HttpStatus.CONFLICT, "ENROLL_006", "이미 취소된 신청입니다."),
	ENROLLMENT_EMPTY(HttpStatus.NOT_FOUND, "ENROLL_007", "신청 완료한 강의가 없습니다."),
	ENROLLMENT_LIST_ACCESS_DENIED(HttpStatus.FORBIDDEN, "ENROLL_008", "본인의 수강 목록만 조회할 수 있습니다."),
	ENROLLMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "ENROLL_009", "신청 정보를 찾을 수 없습니다.");

	// Cart
	private final HttpStatus httpStatus;
	private final String code;
	private final String message;
}



