package wanted.jjsbd.lxpmvc.member.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import wanted.jjsbd.lxpmvc.cart.service.CartService;
import wanted.jjsbd.lxpmvc.common.exception.CustomException;
import wanted.jjsbd.lxpmvc.common.exception.ErrorCode;
import wanted.jjsbd.lxpmvc.config.security.SecuritySessionManager;
import wanted.jjsbd.lxpmvc.member.domain.AuthInfo;
import wanted.jjsbd.lxpmvc.member.dto.LoginRequest;
import wanted.jjsbd.lxpmvc.member.dto.MemberProfileRequest;
import wanted.jjsbd.lxpmvc.member.dto.MemberResponse;
import wanted.jjsbd.lxpmvc.member.dto.SignupRequest;
import wanted.jjsbd.lxpmvc.member.service.MemberService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {
	private final MemberService memberService;
	private final CartService cartService;
	private final SecuritySessionManager securitySessionManager;

	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("title", "로그인");
		model.addAttribute("loginRequest", LoginRequest.empty());
		return "member/login";
	}

	@PostMapping("/login")
	public String doLogin(@Valid @ModelAttribute("loginRequest") LoginRequest request,
		BindingResult bindingResult, HttpServletRequest servletRequest) {
		if (bindingResult.hasErrors()) {
			return "member/login";
		}
		log.info("[LoginFlow] 로그인 요청 진입");
		try {
			AuthInfo authInfo = memberService.login(request);
			securitySessionManager.loginAndSyncSession(authInfo, servletRequest);
		} catch (CustomException e) {
			log.info("[LoginFlow] 로그인 비즈니스 검증 실패 - 에러코드: {}, 메시지: {}", e.getErrorCode(), e.getMessage());
			bindingResult.rejectValue("email", e.getErrorCode().name(), e.getMessage());
			return "member/login";
		}
		log.info("[LoginFlow] 메인 강의 페이지(/courses)로 리다이렉트합니다.");
		return "redirect:/courses";
	}

	@PostMapping("/logout")
	public String logout(HttpServletRequest servletRequest) {
		securitySessionManager.logoutAndInvalidateSession(servletRequest);
		return "redirect:/courses";
	}

	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title", "회원가입");
		model.addAttribute("signupRequest", SignupRequest.empty());
		return "member/signup";
	}

	@PostMapping("/signup")
	public String doSignup(@Valid @ModelAttribute("signupRequest") SignupRequest request,
		BindingResult bindingResult, HttpServletRequest servletRequest) {
		if (bindingResult.hasErrors()) {
			return "member/signup";
		}
		try {
			AuthInfo authInfo = memberService.signup(request.toMemberCreateRequest());
			securitySessionManager.loginAndSyncSession(authInfo, servletRequest);
		} catch (CustomException e) {
			log.info("[SignupFlow] 회원가입 비즈니스 검증 실패 - 에러코드: {}", e.getErrorCode());
			bindingResult.rejectValue("email", e.getErrorCode().name(), e.getMessage());
			return "member/signup";
		}
		log.info("[SignupFlow] 자동 로그인 완료. 메인 강의 페이지(/courses)로 이동합니다.");
		return "redirect:/courses";
	}

	@GetMapping("/profile")
	public String profile(@AuthenticationPrincipal AuthInfo authInfo, Model model) {
		MemberResponse member = fillProfileModel(authInfo.memberId(), model);
		model.addAttribute("memberProfileRequest", new MemberProfileRequest(member.nickname()));
		return "member/edit";
	}

	@PostMapping("/profile")
	public String saveProfile(
		@AuthenticationPrincipal AuthInfo authInfo,
		@Valid @ModelAttribute("memberProfileRequest") MemberProfileRequest request,
		BindingResult bindingResult,
		Model model,
		HttpServletRequest servletRequest
	) {
		if (bindingResult.hasErrors()) {
			fillProfileModel(authInfo.memberId(), model);
			log.info("[ProfileFlow] 프로필 수정 검증 실패 - 규칙 위반");
			return "member/edit";
		}
		try {
			memberService.updateProfile(authInfo.memberId(), request.nickname());
			securitySessionManager.updateSessionNickname(authInfo, request.nickname(), servletRequest);
			log.info("[ProfileFlow] 프로필 수정 및 세션 동기화 완료 - 변경된 닉네임: {}", request.nickname());
		} catch (CustomException e) {
			log.info("[ProfileFlow] 프로필 수정 비즈니스 예외 발생 - 에러코드: {}", e.getErrorCode());
			bindingResult.rejectValue("nickname", e.getErrorCode().name(), e.getMessage());
			fillProfileModel(authInfo.memberId(), model);
			return "member/edit";
		}
		return "redirect:/profile";
	}

	@DeleteMapping("/profile")
	public String withdrawProfile(
		@AuthenticationPrincipal AuthInfo authInfo,
		Model model,
		HttpServletRequest servletRequest
	) {
		try {
			memberService.withdraw(authInfo.memberId());
			log.info("[WithdrawFlow] 회원 탈퇴 처리 성공 - memberId: {}", authInfo.memberId());
		} catch (CustomException e) {
			if (e.getErrorCode() == ErrorCode.MEMBER_ALREADY_WITHDRAWN) {
				log.info("[WithdrawFlow] 이미 탈퇴 완료된 회원 - 세션 강제 만료 처리");
				securitySessionManager.logoutAndInvalidateSession(servletRequest);
				return "redirect:/login";
			}
			if (e.getErrorCode() == ErrorCode.MEMBER_WITHDRAW_FAILED) {
				log.info("[WithdrawFlow] 내부 정책에 의한 탈퇴 실패 - memberId: {}", authInfo.memberId());
				fillProfileModel(authInfo.memberId(), model);
				model.addAttribute("errorMessage", e.getMessage());
				return "member/edit";
			}
			throw e;
		}
		securitySessionManager.logoutAndInvalidateSession(servletRequest);
		return "redirect:/login";
	}

	private MemberResponse fillProfileModel(Long memberId, Model model) {
		MemberResponse member = memberService.getProfile(memberId);
		model.addAttribute("title", "내 정보");
		model.addAttribute("member", member);
		model.addAttribute("cartCount", cartService.getCart(memberId).cartItems().size());
		return member;
	}
}
