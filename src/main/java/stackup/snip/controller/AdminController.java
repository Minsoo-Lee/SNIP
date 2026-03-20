package stackup.snip.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import stackup.snip.dto.member.MemberDetailDto;
import stackup.snip.dto.member.MemberListDto;
import stackup.snip.dto.member.MemberSaveDto;
import stackup.snip.exception.admin.PasswordMismatchException;
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

    @PostMapping("/members/save")
    public String saveMember(
            @ModelAttribute MemberSaveDto memberSaveDto,
            RedirectAttributes redirectAttributes
    ) {
        log.info("memberSaveDto = ", memberSaveDto.toString());
        // ID가 없는 경우 => 새로 등록하는 경우
        if (memberSaveDto.getId() == null) {
            try {
                memberService.saveMember(memberSaveDto);
                return "redirect:/admin/manage?tab=members";
            } catch (PasswordMismatchException e) {
                redirectAttributes.addFlashAttribute("passwordError", "비밀번호가 일치하지 않습니다.");
                redirectAttributes.addFlashAttribute("selectedMember", memberSaveDto);
                return "redirect:/admin/manage?tab=members";
            }
        }
        // ID가 있는 경우 => 수정하는 경우 (nickname, password)
        else {
//            if (!memberSaveDto.getConfirmPassword().equals(memberSaveDto.getNewPassword()))
//                redirectAttributes.addFlashAttribute("error");

//            memberService.updateMember(memberSaveDto);

        }
        return null;
    }
}
