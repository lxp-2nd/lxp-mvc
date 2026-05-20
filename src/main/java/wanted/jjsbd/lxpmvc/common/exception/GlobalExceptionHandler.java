package wanted.jjsbd.lxpmvc.common.exception;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public String handleValidationException(MethodArgumentNotValidException e, RedirectAttributes redirectAttributes) {
		try {
			FieldError fieldError = e.getBindingResult().getFieldError();
			String errorTarget = (fieldError != null && fieldError.getDefaultMessage() != null)
				? fieldError.getDefaultMessage()
				: "INVALID_INPUT";
			String errorMessage;
			try {
				ErrorCode errorCode = ErrorCode.valueOf(errorTarget);
				errorMessage = errorCode.getMessage();
				log.debug(errorCode.toString());
			} catch (IllegalArgumentException ex) {
				errorMessage = errorTarget;
				log.debug(errorTarget);
			}
			redirectAttributes.addAttribute("error", true);
			redirectAttributes.addAttribute("message", errorMessage);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			redirectAttributes.addAttribute("error", true);
			redirectAttributes.addAttribute("message", ErrorCode.INVALID_INPUT.getMessage());
		}
		return "redirect:/login";
	}

	@ExceptionHandler(Exception.class)
	public String handleException(Exception e) {
		log.error(e.getMessage(), e);
		return "/";
	}
}
