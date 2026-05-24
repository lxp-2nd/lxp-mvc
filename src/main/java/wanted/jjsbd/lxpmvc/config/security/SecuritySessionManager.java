package wanted.jjsbd.lxpmvc.config.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import wanted.jjsbd.lxpmvc.member.domain.AuthInfo;

@Component
public class SecuritySessionManager {

	public void loginAndSyncSession(AuthInfo authInfo, HttpServletRequest servletRequest) {
		UsernamePasswordAuthenticationToken authenticationToken =
			new UsernamePasswordAuthenticationToken(authInfo, null, authInfo.getAuthorities());
		SecurityContext securityContext = SecurityContextHolder.getContext();
		securityContext.setAuthentication(authenticationToken);
		servletRequest.changeSessionId();
		HttpSession session = servletRequest.getSession(true);
		session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
	}

	public void logoutAndInvalidateSession(HttpServletRequest servletRequest) {
		SecurityContextHolder.clearContext();
		HttpSession session = servletRequest.getSession(false);
		if (session != null) {
			session.invalidate();
		}
	}
}
