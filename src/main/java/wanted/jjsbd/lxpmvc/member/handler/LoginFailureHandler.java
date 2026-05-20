package com.ohgiraffers.session.user.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 로그인 실패 처리 핸들러.
 * <p>
 * Spring Security가 던지는 추상적인 {@link AuthenticationException}을
 * 사용자에게 보여줄 한국어 메시지로 변환한 뒤 {@code /auth/fail}로 리다이렉트한다.
 * <p>
 * 보안상 "아이디 없음"과 "비밀번호 불일치"를 굳이 구분해 노출하지 않는 편이 안전하지만,
 * 학습용으로 예외 종류별 분기를 명시적으로 보여 둔다.
 */
@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException, ServletException {

        String errorMessage;

        if (exception instanceof BadCredentialsException) {
            // 비밀번호 불일치 또는 사용자 미존재 시 일반적으로 던져진다.
            errorMessage = "아이디가 존재하지 않거나 비밀번호가 일치하지 않습니다.";
        } else if (exception instanceof InternalAuthenticationServiceException) {
            // UserDetailsService 내부에서 런타임 예외가 발생한 경우 — DB 장애 등.
            errorMessage = "서버에서 오류가 발생되었습니다.";
        } else if (exception instanceof UsernameNotFoundException) {
            errorMessage = "존재하지 않는 아이디입니다.";
        } else if (exception instanceof AuthenticationCredentialsNotFoundException) {
            // SecurityContext에 인증 정보가 없을 때 — 보통 잘못된 요청.
            errorMessage = "인증 요청이 거부되었습니다.";
        } else {
            errorMessage = "알 수 없는 오류로 로그인 요청을 처리할 수 없습니다.";
        }

        // 한글 메시지를 쿼리스트링으로 안전하게 실어 보내기 위해 URL 인코딩.
        String encoded = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);
        setDefaultFailureUrl("/auth/fail?message=" + encoded);

        super.onAuthenticationFailure(request, response, exception);
    }
}
