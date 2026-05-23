package wanted.jjsbd.lxpmvc.common.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public String handleCustomException(
		CustomException e,
		HttpServletRequest request,
		RedirectAttributes redirectAttributes
	) {
		ErrorCode errorCode = e.getErrorCode();
		log.debug(errorCode.toString());

		redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

		if (e.getRedirectUrl() != null) {
			return "redirect:" + e.getRedirectUrl();
		}

		return "error/error";
	}

	@ExceptionHandler(Exception.class)
	public String handleException(Exception ex) {
		log.error(ex.getMessage(), ex);
		return "/";
	}
}
