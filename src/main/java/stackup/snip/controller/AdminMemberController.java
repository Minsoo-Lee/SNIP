package stackup.snip.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import stackup.snip.dto.member.MemberFormDto;
import stackup.snip.dto.member.MemberListDto;
import stackup.snip.service.MemberService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/members")
@Slf4j
public class AdminMemberController {

    private final MemberService memberService;

    @GetMapping
    public String memberList(
            @RequestParam(defaultValue = "active") String filter,
            Model model
    ) {
        List<MemberListDto> members = switch (filter) {
            case "deleted" -> memberService.getAllDeletedMembers();
            case "all" -> memberService.getAllMembers();
            default -> memberService.getAllActiveMembers();
        };
        model.addAttribute("members", members);
        model.addAttribute("memberForm", new MemberFormDto());
        model.addAttribute("filter", filter);

        return "sidebar/admin/members";
    }

    @PostMapping
    public String saveMember(
            @ModelAttribute("memberForm") MemberFormDto memberForm,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (!memberForm.getPassword().equals(memberForm.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "settings.password.confirm.notMatch");
        }
        if (result.hasErrors()) {
            log.info("에러 발생!!!!!");
            log.info("에러 목록: {}", result.getAllErrors()); // ✅ 추가
//            model.addAttribute("members", memberService.getAllDeletedMembers());
            model.addAttribute("members", memberService.getAllActiveMembers());
            model.addAttribute("memberForm", memberForm);
            model.addAttribute("currentTab", "members");
            return "sidebar/admin/members";
        }
        memberService.save(memberForm);
        redirectAttributes.addFlashAttribute("successMessage", "성공적으로 저장하였습니다.");  // ✅ 추가
        return "redirect:/admin/members";
    }

    @GetMapping("/{id}")
    public String memberDetail(
            @PathVariable Long id,
            Model model
    ) {
        log.info("model = {}", model.asMap());
        List<MemberListDto> members = memberService.getAllActiveMembers();
        MemberFormDto memberDetailDto = memberService.getMemberById(id);
        model.addAttribute("members", members);
        model.addAttribute("memberForm", memberDetailDto);
        model.addAttribute("filter", "active");
        model.addAttribute("selectedId", id);
        return "sidebar/admin/members";
    }

    @PostMapping("/{id}")
    public String memberEdit(
            @PathVariable Long id,
            @ModelAttribute("memberForm") MemberFormDto memberForm,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (!memberForm.getConfirmPassword().equals(memberForm.getPassword())) {
            result.rejectValue("confirmPassword", "settings.password.confirm.notMatch");
            log.info("errors = {}", result.getAllErrors());
        }
        if (result.hasErrors()) {
            memberForm.setId(id);
            model.addAttribute("memberForm", memberForm);
            model.addAttribute("members", memberService.getAllDeletedMembers());
            model.addAttribute("selectedId", id);
            model.addAttribute("currentTab", "members");
            return "sidebar/admin/members";
        }
        memberService.changePassword(id, memberForm.getPassword());
        if (memberForm.getNickname() != null) {
            memberService.changeNickname(id, memberForm.getNickname());
        }
        redirectAttributes.addFlashAttribute("successMessage", "수정이 완료되었습니다.");  // ✅ 추가
        return "redirect:/admin/members/{id}";
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        memberService.deleteMember(id);
        return "redirect:/admin/members";
    }
}
