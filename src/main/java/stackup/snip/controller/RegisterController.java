package stackup.snip.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import stackup.snip.dto.member.MemberRegisterDto;
import stackup.snip.entity.Member;
import stackup.snip.exception.login.EmailDuplicateException;
import stackup.snip.exception.login.NicknameDuplicateException;
import stackup.snip.service.MemberService;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class RegisterController {

    private final MemberService memberService;

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("memberRegisterDto", new MemberRegisterDto());
        return "login/registerForm";
    }

    @PostMapping("/register")
    public String register(
            @ModelAttribute MemberRegisterDto dto,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Member member = memberService.register(dto.getEmail(), dto.getNickname(), dto.getPassword(), LocalDateTime.now(), 1);
            return "redirect:/home";
        } catch (EmailDuplicateException e) {
            redirectAttributes.addFlashAttribute("emailError", "member.email.duplicate");
            return "redirect:/register";
        } catch (NicknameDuplicateException e) {
            redirectAttributes.addFlashAttribute("nicknameError", "member.nickname.duplicate");
            return "redirect:/register";
        }
    }
}
