package wanted.jjsbd.lxpmvc.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import wanted.jjsbd.lxpmvc.member.dto.LoginRequest;

@Controller
public class MemberController {

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
}
