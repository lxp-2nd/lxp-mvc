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
    COURSE_TITLE_REQUIRED(HttpStatus.BAD_REQUEST, "COURSE_001", "강의 제목은 필수/비어있을 수 없습니다."),
    COURSE_INSTRUCTOR_REQUIRED(HttpStatus.BAD_REQUEST, "COURSE_002", "강사 정보는 필수입니다."),

    SECTION_COURSE_REQUIRED(HttpStatus.BAD_REQUEST, "SECTION_001", "소속된 강의 정보는 필수입니다."),
    SECTION_TITLE_REQUIRED(HttpStatus.BAD_REQUEST, "SECTION_002", "섹션 제목은 필수/비어있을 수 없습니다."),
    SECTION_SEQUENCE_INVALID(HttpStatus.BAD_REQUEST, "SECTION_003", "섹션 순서는 1 이상의 유효한 값이어야 합니다."),

    MATERIAL_SECTION_REQUIRED(HttpStatus.BAD_REQUEST, "MATERIAL_001", "소속된 섹션 정보는 필수입니다."),
    MATERIAL_TITLE_REQUIRED(HttpStatus.BAD_REQUEST, "MATERIAL_002", "강의자료 제목은 필수/비어있을 수 없습니다."),
    MATERIAL_TYPE_REQUIRED(HttpStatus.BAD_REQUEST, "MATERIAL_003", "자료 유형(VIDEO/DOCUMENT)은 필수입니다."),
    MATERIAL_SEQUENCE_INVALID(HttpStatus.BAD_REQUEST, "MATERIAL_004", "자료 순서는 1 이상의 유효한 값이어야 합니다.");

    // Enrollment

	// Cart
	;

    private final HttpStatus httpStatus;
	private final String code;
    private final String message;
}
