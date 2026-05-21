package wanted.jjsbd.lxpmvc.member.controller;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import wanted.jjsbd.lxpmvc.common.MockLxpData;
import wanted.jjsbd.lxpmvc.common.exception.CustomException;
import wanted.jjsbd.lxpmvc.member.domain.MemberInfo;
import wanted.jjsbd.lxpmvc.member.dto.LoginRequest;
import wanted.jjsbd.lxpmvc.member.dto.MemberProfileRequest;
import wanted.jjsbd.lxpmvc.member.dto.MemberResponse;
import wanted.jjsbd.lxpmvc.member.dto.SignupRequest;
import wanted.jjsbd.lxpmvc.member.service.MemberService;

@Slf4j
@Controller
public class MemberController {
	private final MockLxpData mockData;
	private final MemberService memberService;

	public MemberController(MockLxpData mockData, MemberService memberService) {
		this.mockData = mockData;
		this.memberService = memberService;
	}

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
			MemberInfo memberInfo = memberService.login(request.email(), request.password());
			log.info("[LoginFlow] 로그인 검증 성공! 회원 닉네임: {}", memberInfo.nickname());
			UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(memberInfo, null, memberInfo.getAuthorities());
			SecurityContext securityContext = SecurityContextHolder.getContext();
			securityContext.setAuthentication(authenticationToken);
			servletRequest.changeSessionId();
			HttpSession session = servletRequest.getSession(true);
			session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
		} catch (CustomException e) {
			log.error("[LoginFlow] 로그인 비즈니스 검증 실패 - 에러코드: {}, 메시지: {}", e.getErrorCode(), e.getMessage());
			bindingResult.rejectValue("email", e.getErrorCode().name(), e.getMessage());
			return "member/login";
		}
		log.info("[LoginFlow] 메인 강의 페이지(/courses)로 리다이렉트합니다.");
		return "redirect:/courses";
	}

	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title", "회원가입");
		model.addAttribute("signupRequest",
			new SignupRequest("", "", "", ""));
		return "member/signup";
	}

	@PostMapping("/signup")
	public String doSignup(SignupRequest request) {
		return "redirect:/courses";
	}

	@GetMapping("/profile")
	public String profile(Model model) {
		MemberResponse member = mockData.member();

		model.addAttribute("title", "내 정보");
		model.addAttribute("member", member);
		model.addAttribute("memberProfileRequest", new MemberProfileRequest(member.name(), member.email()));
		model.addAttribute("cartCount", mockData.cartCourses().size());
		return "member/edit";
	}

	@PostMapping("/profile")
	public String saveProfile(MemberProfileRequest request) {
		return "redirect:/profile";
	}
}
