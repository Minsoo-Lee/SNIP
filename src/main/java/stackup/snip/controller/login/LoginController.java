package stackup.snip.controller.login;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import stackup.snip.dto.member.MemberLoginDto;
import stackup.snip.entity.Member;
import stackup.snip.repository.jpa.MemberJpaRepository;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final MemberJpaRepository memberJpaRepository;

    @GetMapping("/login")
    public String loginForm() {
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute MemberLoginDto dto) {
        String email = dto.getEmail();
          Optional<Member> findMember = memberJpaRepository.findByEmail(dto.getEmail());
//        isValidateEmail(email);
        return null;
    }
}
