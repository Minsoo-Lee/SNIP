package stackup.snip.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import stackup.snip.dto.category.*;
import stackup.snip.dto.subjective.CategorySubjectiveDto;
import stackup.snip.service.CategoryService;
import stackup.snip.service.SubjectiveService;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryService categoryService;
    private final SubjectiveService subjectiveService;

    @GetMapping
    public String categoryList(
            @ModelAttribute CategorySearchRequestDto dto,
            Model model
    ) {
        setCategoryPage(model, dto, new CategoryFormDto());
        model.addAttribute("mode", "create");
        return "sidebar/admin/categories";
    }

    @PostMapping
    public String saveCategory(
            @ModelAttribute("categoryForm") CategoryFormDto categoryForm,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (categoryService.ifExistsByName(categoryForm.getName())) {
            result.rejectValue("name", null, "[" + categoryForm.getName() + "]: 중복된 카테고리입니다.");
        }
        if (result.hasErrors()) {
            setCategoryPage(model, new CategorySearchRequestDto(), categoryForm);
            return "sidebar/admin/categories";
        }
        categoryService.save(categoryForm.getName());
        redirectAttributes.addFlashAttribute("successMessage", "성공적으로 저장하였습니다.");
        return "redirect:/admin/categories";
    }

    @GetMapping("/{id}")
    public String categoryDetail(
            @PathVariable Long id,
            Model model
    ) {
        CategoryFormDto categoryFormDto = categoryService.getCategoryDetailDtoById(id);
        setCategoryPage(model, new CategorySearchRequestDto(), categoryFormDto);
        model.addAttribute("mode", "edit");
        return "sidebar/admin/categories";
    }

    @PostMapping("/{id}")
    public String categoryEdit(
            @PathVariable Long id,
            @ModelAttribute("category") CategoryFormDto categoryForm,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (categoryService.ifExistsByName(categoryForm.getName())) {
            result.rejectValue("name", null, "[" + categoryForm.getName() + "]: 중복된 카테고리입니다.");
        }
        if (result.hasErrors()) {
            categoryForm.setId(id);
            setCategoryPage(model, new CategorySearchRequestDto(), categoryForm);
            model.addAttribute("mode", "edit");
            return "sidebar/admin/categories";
        }
        categoryService.changeName(id, categoryForm.getName());
        redirectAttributes.addFlashAttribute("successMessage", "수정이 완료되었습니다.");
        return "redirect:/admin/categories/{id}";
    }

    /**
     * 카테고리 지우면 문제까지 지워버리자...
     * 그 전에 어떤 문제가 지워질지 안내창 띄우는게 더 나을지도?
     */
    @GetMapping("/{id}/delete")
    public String showCategoryDelete(
            @PathVariable Long id,
            Model model
    ) {
        CategoryFormDto categoryForm = categoryService.getCategoryDetailDtoById(id);
        List<CategorySubjectiveDto> subjectives = subjectiveService.getCategorySubjectDto(id);

        setCategoryPage(model, new CategorySearchRequestDto(), categoryForm);

        model.addAttribute("mode", "delete");
        model.addAttribute("subjectives", subjectives);
        model.addAttribute("count", subjectives.size());
        return "sidebar/admin/categories";
    }

    @PostMapping("/{id}/delete")
    public String categoryDelete(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        categoryService.softDeleteCategory(id);
        redirectAttributes.addFlashAttribute("successMessage", "삭제가 완료되었습니다.");
        return "redirect:/admin/categories";
    }

    private void setCategoryPage(
            Model model,
            CategorySearchRequestDto cond,
            CategoryFormDto categoryForm
    ) {
        Page<CategoryListDto> page = categoryService.getCategoriesByCondition(cond);

        int currentPage = page.getNumber(); // 0-base
        int blockSize = 10;

        int startPage = (currentPage / blockSize) * blockSize;
        int endPage = Math.min(startPage + blockSize - 1, page.getTotalPages() - 1);

        model.addAttribute("categoryForm", categoryForm);
        model.addAttribute("categories", page.getContent());
        model.addAttribute("page", page);

        // 🔥 블록 페이징용 추가
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        model.addAttribute("cond", cond);
        model.addAttribute("currentTab", "categories");
    }
}
