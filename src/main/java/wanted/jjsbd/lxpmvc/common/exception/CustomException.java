package wanted.jjsbd.lxpmvc.common.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

	private final ErrorCode errorCode;
	private final String redirectUrl;

	public CustomException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
		this.redirectUrl = null;
	}

	// URL을 지정할 수 있는 생성자
	public CustomException(ErrorCode errorCode, String redirectUrl) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
		this.redirectUrl = redirectUrl;
	}
}
