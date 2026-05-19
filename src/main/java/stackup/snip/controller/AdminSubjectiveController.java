package stackup.snip.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import stackup.snip.dto.category.CategorySelectDto;
import stackup.snip.dto.subjective.AdminSubjectiveDto;
import stackup.snip.dto.subjective.SaveSubjectiveDto;
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
        List<CategorySelectDto> categories = categoryService.getAllActiveCategories().stream().map(c -> new CategorySelectDto(c.getName())).toList();
        List<AdminSubjectiveDto> subjectives = switch (filter) {
            case "active" -> subjectiveService.getAllActiveSubjectives();
            case "deleted" -> subjectiveService.getAllDeletedSubjectives();
            default -> subjectiveService.getAllSubjectives();
        };
        model.addAttribute("categories", categories);
        model.addAttribute("subjectives", subjectives);
        model.addAttribute("filter", filter);
        model.addAttribute("mode", "create");
        model.addAttribute("subjectiveForm", new SaveSubjectiveDto());

        return "sidebar/admin/subjectives";
    }

    @PostMapping
    public String saveSubjective(
            @ModelAttribute("subjectiveForm") SaveSubjectiveDto dto,
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

}
