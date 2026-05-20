package wanted.jjsbd.lxpmvc.common.exception;

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

	@ExceptionHandler(Exception.class)
	public String handleException(Exception e) {
		log.error(e.getMessage(), e);
		return "/";
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidationException(MethodArgumentNotValidException e, RedirectAttributes redirectAttributes) {
        try {
            String errorTarget = e.getBindingResult().getFieldError().getDefaultMessage();
            ErrorCode errorCode = ErrorCode.valueOf(errorTarget); 
            redirectAttributes.addAttribute("error", true);
            redirectAttributes.addAttribute("message", errorCode.getMessage()); 
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            redirectAttributes.addAttribute("error", true);
            redirectAttributes.addAttribute("message", ErrorCode.INVALID_INPUT.getMessage());
        }
        return "redirect:/login";
    }
}
