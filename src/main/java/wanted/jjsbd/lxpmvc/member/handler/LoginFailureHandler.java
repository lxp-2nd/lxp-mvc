package wanted.jjsbd.lxpmvc.member.handler;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import wanted.jjsbd.lxpmvc.common.exception.ErrorCode;

@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {
	private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException exception) throws IOException {
		String encodedMessage = URLEncoder.encode(ErrorCode.MEMBER_INVALID_CREDENTIALS.getMessage(),
			StandardCharsets.UTF_8);
		redirectStrategy.sendRedirect(request, response, "/login?error=true&message=" + encodedMessage);
	}
}