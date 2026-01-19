package stackup.snip.controller.login;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import stackup.snip.dto.member.MemberLoginDto;
import stackup.snip.entity.Member;
import stackup.snip.exception.login.LoginFailException;
import stackup.snip.exception.login.LoginPasswordNotMatchException;
import stackup.snip.service.LoginService;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/")
    public String loginForm() {
        return "login/loginForm";
    }

    @PostMapping("/")
    public String login(
            @ModelAttribute MemberLoginDto dto,
            BindingResult result
    ) {
        try {
            // 추후에 세션에서 활용
            Member member = loginService.login(dto.getEmail(), dto.getPassword());
            return "redirect:/home";
        } catch (LoginFailException e) {
            result.reject("loginFail", "아이디 또는 비밀번호가 올바르지 않습니다.");
            return "login/loginForm";
        }
}
