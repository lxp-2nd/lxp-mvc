package wanted.jjsbd.lxpmvc.common.exception;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

	private static final String ERROR_VIEW = "error/error";

	@ExceptionHandler(CustomException.class)
	public String handleCustomException(
		CustomException ex,
		HttpServletRequest request,
		HttpServletResponse response,
		Model model,
		RedirectAttributes redirectAttributes
	) {
		ErrorCode errorCode = ex.getErrorCode();
		log.debug("CustomException: code={}, path={}", errorCode.getCode(), request.getRequestURI(), ex);

		if (ex.getRedirectUrl() != null) {
			redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());

			String redirectUrl = ex.getRedirectUrl();
			if (redirectUrl.startsWith("/") && !redirectUrl.startsWith("//")) {
				return "redirect:" + redirectUrl;
			}
			log.warn("허용되지 않은 redirectUrl: {}", redirectUrl);
			addErrorAttributes(model, request, response, ErrorCode.INVALID_INPUT, "허용되지 않은 리다이렉트 경로입니다.");
			return ERROR_VIEW;
		}

		addErrorAttributes(model, request, response, errorCode, ex.getMessage());
		return ERROR_VIEW;
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler({
		NoResourceFoundException.class,
		NoHandlerFoundException.class,
		MethodArgumentTypeMismatchException.class
	})
	public String handleNotFoundException(
		Exception ex,
		HttpServletRequest request,
		HttpServletResponse response,
		Model model
	) {
		log.debug("Not found: path={}", request.getRequestURI(), ex);

		addErrorAttributes(model, request, response, HttpStatus.NOT_FOUND, "요청한 페이지를 찾을 수 없습니다.");
		return ERROR_VIEW;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public String handleMethodArgumentNotValidException(
		MethodArgumentNotValidException ex,
		HttpServletRequest request,
		HttpServletResponse response,
		Model model
	) {
		log.debug("Request body validation failed: path={}", request.getRequestURI(), ex);

		addErrorAttributes(model, request, response, ErrorCode.INVALID_INPUT, getFieldErrorMessage(ex));
		return ERROR_VIEW;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BindException.class)
	public String handleBindException(
		BindException ex,
		HttpServletRequest request,
		HttpServletResponse response,
		Model model
	) {
		log.debug("Request binding failed: path={}", request.getRequestURI(), ex);

		addErrorAttributes(model, request, response, ErrorCode.INVALID_INPUT, getFieldErrorMessage(ex));
		return ERROR_VIEW;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({
		ConstraintViolationException.class,
		HandlerMethodValidationException.class,
		MissingServletRequestParameterException.class
	})
	public String handleBadRequestException(
		Exception ex,
		HttpServletRequest request,
		HttpServletResponse response,
		Model model
	) {
		log.debug("Bad request: path={}", request.getRequestURI(), ex);

		addErrorAttributes(model, request, response, ErrorCode.INVALID_INPUT, ErrorCode.INVALID_INPUT.getMessage());
		return ERROR_VIEW;
	}

	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public String handleMethodNotAllowedException(
		HttpRequestMethodNotSupportedException ex,
		HttpServletRequest request,
		HttpServletResponse response,
		Model model
	) {
		log.debug("Method not allowed: method={}, path={}", request.getMethod(), request.getRequestURI(), ex);

		addErrorAttributes(model, request, response, ErrorCode.METHOD_NOT_ALLOWED,
			ErrorCode.METHOD_NOT_ALLOWED.getMessage());
		return ERROR_VIEW;
	}

	@ExceptionHandler(ResponseStatusException.class)
	public String handleResponseStatusException(
		ResponseStatusException ex,
		HttpServletRequest request,
		HttpServletResponse response,
		Model model
	) {
		log.debug("ResponseStatusException: status={}, path={}", ex.getStatusCode(), request.getRequestURI(), ex);

		HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
		if (status == null) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		addErrorAttributes(model, request, response, status, getResponseStatusMessage(ex, status));
		return ERROR_VIEW;
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public String handleException(
		Exception ex,
		HttpServletRequest request,
		HttpServletResponse response,
		Model model
	) {
		log.error("Unhandled exception: path={}", request.getRequestURI(), ex);

		addErrorAttributes(model, request, response, ErrorCode.INTERNAL_SERVER_ERROR,
			ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
		return ERROR_VIEW;
	}

	private void addErrorAttributes(
		Model model,
		HttpServletRequest request,
		HttpServletResponse response,
		ErrorCode errorCode,
		String errorMessage
	) {
		addErrorAttributes(model, request, response, errorCode.getHttpStatus(), errorMessage);
	}

	private void addErrorAttributes(
		Model model,
		HttpServletRequest request,
		HttpServletResponse response,
		HttpStatus status,
		String errorMessage
	) {
		response.setStatus(status.value());
		model.addAttribute("title", "오류");
		model.addAttribute("statusCode", status.value());
		model.addAttribute("statusReason", status.getReasonPhrase());
		model.addAttribute("errorMessage", errorMessage);
		model.addAttribute("requestUri", request.getRequestURI());
	}

	private String getFieldErrorMessage(BindException ex) {
		if (!ex.hasFieldErrors()) {
			return ErrorCode.INVALID_INPUT.getMessage();
		}

		return ex.getFieldErrors()
			.stream()
			.map(this::formatFieldError)
			.collect(Collectors.joining(", "));
	}

	private String formatFieldError(FieldError fieldError) {
		return fieldError.getField() + ": " + fieldError.getDefaultMessage();
	}

	private String getResponseStatusMessage(ResponseStatusException ex, HttpStatus status) {
		if (ex.getReason() == null || ex.getReason().isBlank()) {
			return status.getReasonPhrase();
		}
		return ex.getReason();
	}
}
