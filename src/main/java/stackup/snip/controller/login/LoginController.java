package stackup.snip.controller.login;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import stackup.snip.dto.member.MemberLoginDto;
import stackup.snip.entity.Member;
import stackup.snip.exception.login.LoginFailException;
import stackup.snip.service.MemberService;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final MemberService memberService;
    private static final String ADMIN_EMAIL = "admin@admin.dev";

    @GetMapping("/")
    public String loginForm(Model model) {
        model.addAttribute("memberLoginDto", new MemberLoginDto());
        return "login/loginForm";
    }

    @PostMapping("/")
    public String login(
            @ModelAttribute MemberLoginDto dto,
            RedirectAttributes redirectAttributes,
            HttpSession session
    ) {
        try {
            // 추후에 세션에서 활용
            Member member = memberService.login(dto.getEmail(), dto.getPassword());
            session.setAttribute("isAdmin", dto.getEmail().equals(ADMIN_EMAIL));
            return "redirect:/home";
        } catch (LoginFailException e) {
            redirectAttributes.addFlashAttribute(
                    "loginError", "login.fail"
            );
            return "redirect:/";
        }
    }
}
