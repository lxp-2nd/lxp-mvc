package wanted.jjsbd.lxpmvc.common.domain;

import java.util.Collection;

import wanted.jjsbd.lxpmvc.common.exception.CustomException;
import wanted.jjsbd.lxpmvc.common.exception.ErrorCode;

/**
 * 이것도 만들어주세요
 */
public class DomainValidator {
	/**
	 * 1. 문자열 필수값 검증 (null 또는 공백 방지)
	 */
	public static void validateNotBlank(String value) {
		if (value == null || value.isBlank()) {
			throw new CustomException(ErrorCode.REQUIRED_VALUE_MISSING);
		}
	}

	/**
	 * 2. 객체 필수값 검증 (null 방지)
	 */
	public static void validateNotNull(Object obj) {
		if (obj == null) {
			throw new CustomException(ErrorCode.REQUIRED_VALUE_MISSING);
		}
	}

	/**
	 * 3. 양수 검증 (1 이상인지 확인)
	 */
	public static void validatePositive(Number number) {
		if (number == null || number.longValue() < 1) {
			throw new CustomException(ErrorCode.INVALID_NUMBER_RANGE);
		}
	}

	/**
	 * 4. 컬렉션 비어있음 검증
	 */
	public static void validateNotEmpty(Collection<?> collection) {
		if (collection == null || collection.isEmpty()) {
			throw new CustomException(ErrorCode.EMPTY_LIST);
		}
	}

}

