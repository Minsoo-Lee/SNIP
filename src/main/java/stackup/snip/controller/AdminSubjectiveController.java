package stackup.snip.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import stackup.snip.dto.category.CategoryListDto;
import stackup.snip.dto.category.CategorySelectDto;
import stackup.snip.dto.subjective.*;
import stackup.snip.service.CategoryService;
import stackup.snip.service.SubjectiveService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/subjectives")
@Slf4j
public class AdminSubjectiveController {

    private final SubjectiveService subjectiveService;
    private final CategoryService categoryService;

    @GetMapping
    public String subjectiveList(
            @ModelAttribute SubjectiveSearchRequestDto dto,
            Model model) {
        setSubjectivePage(model, dto, new SubjectiveFormDto());

        model.addAttribute("mode", "create");

        return "sidebar/admin/subjectives";
    }

    @PostMapping
    public String saveSubjective(
            @ModelAttribute("subjectiveForm") SubjectiveFormDto dto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
       if (subjectiveService.ifExistsByContent(dto.getContent())) {
           result.rejectValue("content", null, "[" + dto.getContent() + "]: 중복된 문제입니다.");
       }
       if (result.hasErrors()) {
           setSubjectivePage(model, new SubjectiveSearchRequestDto(), dto);
           return "sidebar/admin/subjectives";
       }
       redirectAttributes.addFlashAttribute("successMessage", "성공적으로 저장하였습니다.");
       subjectiveService.save(dto.getCategoryName(), dto.getContent());
       return "redirect:/admin/subjectives";
    }

    @GetMapping("/{id}")
    public String subjectiveDetail(
            @PathVariable Long id,
            Model model
    ) {
        SubjectiveFormDto subjectiveDetailDto = subjectiveService.getSubjectDetailById(id);
        setSubjectivePage(model, new SubjectiveSearchRequestDto(), subjectiveDetailDto);
        model.addAttribute("mode", "edit");
        return "sidebar/admin/subjectives";
    }

    @PostMapping("/{id}")
    public String subjectiveEdit(
            @PathVariable Long id,
            @ModelAttribute("subjective") SubjectiveFormDto subjectiveForm,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (subjectiveService.ifExistsByContent(subjectiveForm.getContent())) {
            result.rejectValue("content", null, "[" + subjectiveForm.getContent() + "]: 중복된 문제입니다.");
        }
        if (result.hasErrors()) {
            subjectiveForm.setId(id);
            setSubjectivePage(model, new SubjectiveSearchRequestDto(), subjectiveForm);
            model.addAttribute("mode", "edit");
            return "sidebar/admin/subjectives";
        }
        subjectiveService.changeContent(id, subjectiveForm.getContent());
        redirectAttributes.addFlashAttribute("successMessage", "수정이 완료되었습니다.");
        return "redirect:/admin/subjectives/{id}";
    }

    @GetMapping("/{id}/delete")
    public String showSubjectiveDelete(
            @PathVariable Long id,
            Model model
    ) {
        SubjectiveFormDto subjectiveForm = subjectiveService.getSubjectDetailById(id);;

        setSubjectivePage(model, new SubjectiveSearchRequestDto(), subjectiveForm);
        model.addAttribute("mode", "delete");
        return "sidebar/admin/subjectives";
    }

    @PostMapping("/{id}/delete")
    public String subjectiveDelete(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        subjectiveService.softDeleteSubjective(id);
        redirectAttributes.addFlashAttribute("successMessage", "삭제가 완료되었습니다.");
        return "redirect:/admin/subjectives";
    }

    private void setSubjectivePage(
            Model model,
            SubjectiveSearchRequestDto cond,
            SubjectiveFormDto subjectiveForm
    ) {
        List<SubjectiveListDto> subjectives = subjectiveService.getSubjectiveByCondition(cond);

        model.addAttribute("subjectiveForm", subjectiveForm);
        model.addAttribute("subjectives", subjectives);
        model.addAttribute("cond", cond);
        model.addAttribute("currentTab", "subjectives");
    }
}
