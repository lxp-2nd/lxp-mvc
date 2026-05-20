package wanted.jjsbd.lxpmvc.common.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public String handleCustomException(CustomException e) {
		ErrorCode errorCode = e.getErrorCode();
		log.debug(errorCode.toString());
		return "error/error";
	}

	@ExceptionHandler(Exception.class)
	public String handleException(Exception e) {
		log.error(e.getMessage(), e);
		return "/";
	}
}
