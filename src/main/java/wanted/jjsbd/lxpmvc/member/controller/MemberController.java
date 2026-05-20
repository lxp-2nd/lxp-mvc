package wanted.jjsbd.lxpmvc.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import wanted.jjsbd.lxpmvc.common.MockLxpData;
import wanted.jjsbd.lxpmvc.member.dto.LoginRequest;
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
		model.addAttribute("loginRequest", new LoginRequest("", ""));
		return "member/login";
	}

	@PostMapping("/login")
	public String doLogin(@Valid @ModelAttribute("loginRequest") LoginRequest request,
		BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
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
