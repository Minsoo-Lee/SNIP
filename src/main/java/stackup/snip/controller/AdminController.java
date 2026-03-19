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
public class AdminController {

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
            model.addAttribute("tab", "users");
            return "sidebar/admin/adminTab";
        }
        model.addAttribute("error", "settings.password.notMatch");
        return "sidebar/admin/checkAdmin";
    }

    @GetMapping("/manage")
    public String switchTab(
            @RequestParam(defaultValue = "members") String tab,
            Model model
    ) {
        model.addAttribute("tab", tab);
        if (tab.equals("members")) {
            memberService.
            model.addAttribute("users", )
        } else if (tab.equals("subjectives")) {

        }
        return "sidebar/admin/adminTab";
    }

}
