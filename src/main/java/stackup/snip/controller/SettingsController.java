package stackup.snip.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import stackup.snip.service.MemberService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/settings")
@Slf4j
public class SettingsController {

    private final MemberService memberService;

    @GetMapping
    public String settingHome() {
        log.info("settingHome.class");
        return "sidebar/settings/checkPassword";
    }

    @PostMapping("/check")
    public String checkPassword(
            @RequestParam String password,
            Model model,
            @RequestAttribute("loginMemberId") Long memberId) {
        if (memberService.checkMatchPassword(memberId, password)) {
            model.addAttribute("tab", "nickname");
            return "sidebar/settings/settingsTab";
        }
        model.addAttribute("error", "settings.password.notMatch");
        return "sidebar/settings/checkPassword";
    }

    @GetMapping("/account")
    public String switchTab(
            @RequestParam(defaultValue = "nickname") String tab,
            Model model) {
        model.addAttribute("tab", tab);
        return "sidebar/settings/settingsTab";
    }

    @PostMapping("/nickname")
    public String changeNickname(
            @RequestParam String newNickname,
            @RequestAttribute("loginMemberId") Long memberId,
            HttpSession session) {
        memberService.changeNickname(memberId, newNickname);
        session.setAttribute("userName", newNickname);
        return "redirect:/settings/account?tab=nickname";
    }
}
