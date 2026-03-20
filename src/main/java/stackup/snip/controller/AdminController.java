package stackup.snip.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import stackup.snip.dto.member.MemberDetailDto;
import stackup.snip.dto.member.MemberListDto;
import stackup.snip.service.MemberService;

import java.util.List;

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
//            model.addAttribute("tab", "users");
            return "redirect:/admin/manage?tab=members";
        }
        model.addAttribute("error", "settings.password.notMatch");
        return "sidebar/admin/checkAdmin";
    }

    @GetMapping("/manage")
    public String switchTab(
            @RequestParam(defaultValue = "members") String tab,
            @RequestParam(required = false) String memberId,
            Model model
    ) {
        model.addAttribute("tab", tab);
        if (tab.equals("members")) {

            List<MemberListDto> members = memberService.getAllMembers();
            MemberDetailDto memberDetailDto = memberService.getMemberDetailDto(memberId);
            model.addAttribute("members", members);
            model.addAttribute("selectedMember", memberDetailDto);
        } else if (tab.equals("subjectives")) {

        }
        return "sidebar/admin/adminTab";
    }

}
