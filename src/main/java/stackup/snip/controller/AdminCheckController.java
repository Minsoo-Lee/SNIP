package stackup.snip.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import stackup.snip.service.MemberService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@Slf4j
public class AdminCheckController {

    private final MemberService memberService;

    @GetMapping
    public String adminHome() {
        return "sidebar/admin/checkAdmin";
    }

    @PostMapping("/check")
    public String checkAdmin(
            @RequestParam String password,
            Model model,
            @RequestAttribute("loginMemberId") Long memberId
    ) {
        if (memberService.checkAdminPassword(memberId, password)) {
            return "redirect:/admin/members";
        }
        model.addAttribute("error", "settings.password.notMatch");
        return "sidebar/admin/checkAdmin";
    }
}
