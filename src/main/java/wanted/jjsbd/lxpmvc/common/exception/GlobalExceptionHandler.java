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
		CustomException ex,
		HttpServletRequest request,
		RedirectAttributes redirectAttributes
	) {
		ErrorCode errorCode = ex.getErrorCode();
		log.debug(errorCode.toString());

		if (ex.getRedirectUrl() != null) {
			redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());

			String redirectUrl = ex.getRedirectUrl();
			if (redirectUrl.startsWith("/") && !redirectUrl.startsWith("//")) {
				return "redirect:" + redirectUrl;
			}
			log.warn("허용되지 않은 redirectUrl: {}", redirectUrl);
			return "error/error";
		}
		request.setAttribute("errorMessage", ex.getErrorCode().getMessage());

		return "error/error";
	}

	@ExceptionHandler(Exception.class)
	public String handleException(Exception ex) {
		log.error(ex.getMessage(), ex);
		return "/";
	}
}
