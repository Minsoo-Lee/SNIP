package stackup.snip.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import stackup.snip.dto.member.MemberFormDto;
import stackup.snip.dto.member.MemberListDto;
import stackup.snip.dto.member.MemberSearchRequestDto;
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
            @ModelAttribute MemberSearchRequestDto dto,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable,
            Model model
    ) {
        setMemberPage(model, dto, new MemberFormDto());
        model.addAttribute("mode", "create");
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
            setMemberPage(model, new MemberSearchRequestDto(), memberForm);
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
        MemberFormDto memberDetailDto = memberService.getMemberById(id);
        setMemberPage(model, new MemberSearchRequestDto(), memberDetailDto);
        model.addAttribute("mode", "edit");
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
            setMemberPage(model, new MemberSearchRequestDto("deleted"), memberForm);
            model.addAttribute("mode", "edit");
            return "sidebar/admin/members";
        }
        memberService.changePassword(id, memberForm.getPassword());
        if (memberForm.getNickname() != null) {
            memberService.changeNickname(id, memberForm.getNickname());
        }
        redirectAttributes.addFlashAttribute("successMessage", "수정이 완료되었습니다.");  // ✅ 추가
        return "redirect:/admin/members/{id}";
    }

    @GetMapping("/{id}/delete")
    public String showMemberDelete(
            @PathVariable Long id,
            Model model
    ) {
        MemberFormDto memberForm = memberService.getMemberById(id);
        setMemberPage(model, new MemberSearchRequestDto(), memberForm);
        model.addAttribute("mode", "delete");
        return "sidebar/admin/members";
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
            ) {
        memberService.deleteMember(id);
        redirectAttributes.addFlashAttribute("successMessage", "삭제가 완료되었습니다.");
        return "redirect:/admin/members";
    }

    private void setMemberPage(Model model, MemberSearchRequestDto dto, MemberFormDto memberForm) {
        Page<MemberListDto> page =
                memberService.getMembersByCondition(dto);

        int currentPage = page.getNumber(); // 0-base
        int blockSize = 10;

        int startPage = (currentPage / blockSize) * blockSize;
        int endPage = Math.min(startPage + blockSize - 1, page.getTotalPages() - 1);

        model.addAttribute("memberForm", memberForm);
        model.addAttribute("members", page.getContent());
        model.addAttribute("page", page);

        // 🔥 블록 페이징용 추가
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        model.addAttribute("cond", dto);
        model.addAttribute("currentTab", "members");
    }
}
