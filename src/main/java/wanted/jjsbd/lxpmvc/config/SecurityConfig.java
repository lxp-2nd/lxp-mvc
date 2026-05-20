package com.ohgiraffers.session.config;

import com.ohgiraffers.session.user.domain.UserRole;
import com.ohgiraffers.session.user.handler.LoginFailureHandler;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 보안 설정.
 * <p>
 * 세션 기반 인증의 핵심 정책 세 가지를 한곳에서 선언한다.
 * <ol>
 *   <li>URL별 인가 규칙 — 어떤 경로를 누구에게 열어둘지</li>
 *   <li>로그인/로그아웃 처리 흐름 — 폼 로그인 URL, 실패 핸들러, 세션 무효화</li>
 *   <li>세션 동시성 제어 — 동일 사용자가 동시에 가질 수 있는 세션 수</li>
 * </ol>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final LoginFailureHandler loginFailureHandler;

    public SecurityConfig(LoginFailureHandler loginFailureHandler) {
        this.loginFailureHandler = loginFailureHandler;
    }

    /**
     * 비밀번호 단방향 해시용 인코더.
     * <p>
     * BCrypt는 솔트가 해시값에 포함되어 별도 저장이 불필요하며,
     * work factor(연산 비용)를 통해 brute-force 비용을 의도적으로 높인다.
     * 회원가입 시 인코딩, 로그인 시 자동 매칭에 동일 빈이 사용된다.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 정적 리소스(CSS·JS·이미지)는 인증 필터 자체에서 제외한다.
     * <p>
     * SecurityFilterChain의 permitAll과 달리 web.ignoring()은 보안 필터를
     * 아예 거치지 않으므로 성능상 이점이 크고, 캐시 헤더 등에도 영향이 없다.
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    /**
     * HTTP 요청에 적용할 보안 필터 체인을 구성한다.
     */
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {

        http
                // 1) 인가(authorization) 규칙 — 위에서 아래로 평가되므로 순서가 중요하다.
                .authorizeHttpRequests(auth -> {
                    // 비로그인 사용자도 접근 가능한 공개 경로
                    auth.requestMatchers("/auth/login", "/user/signup", "/auth/fail", "/").permitAll();
                    // 관리자 전용 영역 — UserRole.ADMIN의 문자열 값과 일치해야 한다.
                    auth.requestMatchers("/admin/*").hasAnyAuthority(UserRole.ADMIN.getValue());
                    // 일반 회원 전용 영역
                    auth.requestMatchers("/user/*").hasAnyAuthority(UserRole.USER.getValue());
                    // 그 외 모든 요청은 인증 필요
                    auth.anyRequest().authenticated();
                })

                // 2) 폼 기반 로그인 설정
                .formLogin(form -> {
                    // 커스텀 로그인 페이지 경로. GET은 화면, POST는 인증 처리에 같은 URL이 사용된다.
                    form.loginPage("/auth/login");
                    // 로그인 성공 시 항상 "/"로 이동 (true: 이전 요청 URL 무시).
                    form.defaultSuccessUrl("/", true);
                    // 인증 실패 시 사용자 친화적인 메시지로 변환해 /auth/fail로 리다이렉트한다.
                    form.failureHandler(loginFailureHandler);
                    // username/password 파라미터는 Spring Security 기본값을 그대로 사용한다.
                })

                // 3) 로그아웃 설정
                .logout(logout -> {
                    logout.logoutUrl("/auth/logout");
                    // JSESSIONID 쿠키 제거 — 브라우저에 남은 세션 식별자도 함께 정리한다.
                    logout.deleteCookies("JSESSIONID");
                    logout.invalidateHttpSession(true);
                    logout.logoutSuccessUrl("/");
                })

                // 4) 세션 관리 — 동일 사용자 최대 1세션, 만료 시 루트로 이동.
                .sessionManagement(session -> {
                    session.maximumSessions(1);
                    session.invalidSessionUrl("/");
                })

                // 5) CSRF 비활성화 — 학습용 예제이므로 끈다. 실서비스에서는 활성화 필수.
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
