package stackup.snip.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import stackup.snip.dto.member.MemberLoginDto;
import stackup.snip.entity.Member;
import stackup.snip.exception.login.LoginFailException;
import stackup.snip.exception.login.LoginLockException;
import stackup.snip.service.MemberService;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final MemberService memberService;
    private static final String ADMIN_EMAIL = "admin@admin.dev";

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("memberLoginDto", new MemberLoginDto());
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(
            @ModelAttribute MemberLoginDto dto,
            RedirectAttributes redirectAttributes,
            HttpSession session
    ) {
        try {
            // 추후에 세션에서 활용
            Member member = memberService.login(dto.getEmail(), dto.getPassword());
            session.setAttribute("memberId", member.getId());
            session.setAttribute("userName", member.getNickname());
            session.setAttribute("isAdmin", dto.getEmail().equals(ADMIN_EMAIL));
            return "redirect:/";
        } catch (LoginFailException e) {
            redirectAttributes.addFlashAttribute(
                    "loginError", "이메일과 비밀번호가 일치하지 않습니다."
            );
            return "redirect:/login";
        } catch (LoginLockException e) {

            long remainSeconds = e.getRemainSeconds();

            long minutes = remainSeconds / 60;
            long seconds = remainSeconds % 60;

            redirectAttributes.addFlashAttribute(
                    "loginError",
                    String.format(
                            "%d분 %d초 후 다시 시도해주세요.",
                            minutes,
                            seconds
                    )
            );

            return "redirect:/login";
        }
    }

    @PostMapping("/logout")
    public String logout(
            HttpSession session
    ) {
        session.invalidate();
        return "redirect:/";
    }
}
