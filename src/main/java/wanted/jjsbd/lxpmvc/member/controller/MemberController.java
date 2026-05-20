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
import wanted.jjsbd.lxpmvc.common.MockLxpData;
import wanted.jjsbd.lxpmvc.common.exception.CustomException;
import wanted.jjsbd.lxpmvc.member.dto.LoginRequest;
import wanted.jjsbd.lxpmvc.member.dto.MemberInfo;
import wanted.jjsbd.lxpmvc.member.dto.MemberProfileRequest;
import wanted.jjsbd.lxpmvc.member.dto.MemberResponse;
import wanted.jjsbd.lxpmvc.member.dto.SignupRequest;

@Controller
public class MemberController {

	private final MockLxpData mockData;

	public MemberController(MockLxpData mockData) {
		this.mockData = mockData;
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
		try {
			MemberInfo memberInfo = memberService.login(request.email(), request.password());

			UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(memberInfo.getEmail(), null, memberInfo.getAuthorities());

			SecurityContext securityContext = SecurityContextHolder.getContext();
			securityContext.setAuthentication(authenticationToken);

			HttpSession session = servletRequest.getSession(true);
			session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
			session.setMaxInactiveInterval(240 * 60);
		} catch (CustomException e) {
			bindingResult.rejectValue("email", e.getErrorCode().name(), e.getMessage());
			return "member/login";
		}
		return "redirect:/courses";
	}

	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title", "회원가입");
		model.addAttribute("signupRequest", new SignupRequest("", "", ""));
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
