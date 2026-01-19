package stackup.snip.controller.login;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import stackup.snip.dto.member.MemberRegisterDto;
import stackup.snip.entity.Member;
import stackup.snip.service.LoginService;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class RegisterController {

    private final LoginService memberService;

    @GetMapping("/register")
    public String registerForm() {
        return "login/registerForm";
    }


    @PostMapping("/register")
    public String register(@ModelAttribute MemberRegisterDto dto) {
        Member member = new Member(dto.getEmail(), dto.getNickname(), dto.getPassword(), LocalDateTime.now(), 1);
        memberService.register(member);
        return "/home";
    }
}
