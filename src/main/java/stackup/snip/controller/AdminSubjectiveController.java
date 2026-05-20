package stackup.snip.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import stackup.snip.dto.category.CategoryDetailDto;
import stackup.snip.dto.category.CategoryEditDto;
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
            @RequestParam(defaultValue = "active") String filter,
            Model model) {
        setSubjectivePage(model, filter);

        model.addAttribute("mode", "create");
        model.addAttribute("subjectiveForm", new SubjectiveSaveDto());

        return "sidebar/admin/subjectives";
    }

    @PostMapping
    public String saveSubjective(
            @ModelAttribute("subjectiveForm") SubjectiveSaveDto dto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
       if (subjectiveService.ifExistsByContent(dto.getContent())) {
           result.rejectValue("content", null, "[" + dto.getContent() + "]: 중복된 문제입니다.");
       }
       if (result.hasErrors()) {
           model.addAttribute("subjectiveForm", dto);
           model.addAttribute("currentTab", "subjectives");
           model.addAttribute("subjectives", subjectiveService.getAllActiveSubjectives());
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
        SubjectiveDetailDto subjectiveDetailDto = subjectiveService.getSubjectDetailById(id);
        model.addAttribute("subjective", subjectiveDetailDto);
        model.addAttribute("mode", "edit");
        model.addAttribute("subjectives", subjectiveService.getAllActiveSubjectives());
        model.addAttribute("filter", "active");
        model.addAttribute("categories", categoryService.getAllActiveCategories());
        model.addAttribute("currentTab", "subjectives");
        return "sidebar/admin/subjectives";
    }

    @PostMapping("/{id}")
    public String subjectiveEdit(
            @PathVariable Long id,
            @ModelAttribute("subjective") SubjectiveEditDto dto,
            BindingResult result,
            Model model,
            @RequestParam(defaultValue = "active") String filter,
            RedirectAttributes redirectAttributes
    ) {
        if (subjectiveService.ifExistsByContent(dto.getContent())) {
            result.rejectValue("content", null, "[" + dto.getContent() + "]: 중복된 문제입니다.");
        }
        if (result.hasErrors()) {
            dto.setId(id);

            setSubjectivePage(model, filter);

            model.addAttribute("subjective", dto);
            model.addAttribute("mode", "edit");
            return "sidebar/admin/subjectives";
        }
        subjectiveService.changeContent(id, dto.getContent());
        redirectAttributes.addFlashAttribute("successMessage", "수정이 완료되었습니다.");
        return "redirect:/admin/subjectives/{id}";
    }

    @GetMapping("/{id}/delete")
    public String showSubjectiveDelete(
            @PathVariable Long id,
            Model model
    ) {
        SubjectiveDetailDto subjectiveDetailDto = subjectiveService.getSubjectDetailById(id);;

        model.addAttribute("subjective", subjectiveDetailDto);
        model.addAttribute("mode", "delete");
        model.addAttribute("subjectives", subjectiveService.getAllActiveSubjectives());
        model.addAttribute("filter", "active");
        model.addAttribute("currentTab", "subjectives");
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
            String filter
    ) {
        List<CategorySelectDto> categories =
                categoryService.getAllActiveCategories()
                        .stream()
                        .map(c -> new CategorySelectDto(c.getName()))
                        .toList();

        List<AdminSubjectiveDto> subjectives = switch (filter) {
            case "active" -> subjectiveService.getAllActiveSubjectives();
            case "deleted" -> subjectiveService.getAllDeletedSubjectives();
            default -> subjectiveService.getAllSubjectives();
        };

        model.addAttribute("categories", categories);
        model.addAttribute("subjectives", subjectives);
        model.addAttribute("filter", filter);
        model.addAttribute("currentTab", "subjectives");
    }
}
