package stackup.snip.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import stackup.snip.dto.category.CategoryListDto;
import stackup.snip.dto.category.SaveCategoryDto;
import stackup.snip.dto.subjective.AdminSubjectiveDto;
import stackup.snip.service.CategoryService;

import java.util.List;

@Controller
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public String categoryList(Model model) {
        List<CategoryListDto> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("categoryForm", new SaveCategoryDto());
        return "sidebar/admin/categories";
    }

    @PostMapping
    public String saveCategory(
            @ModelAttribute("categoryForm") SaveCategoryDto dto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (categoryService.ifExistsByName(dto.getName())) {
            result.rejectValue("name", null, "[" + dto.getName() + "]: 중복된 카테고리입니다.");
        }
        if (result.hasErrors()) {
            model.addAttribute("categoryForm", dto);
            model.addAttribute("currentTab", "categories");
            model.addAttribute("categories", categoryService.getAllCategories());
            return "sidebar/admin/categories";
        }
        redirectAttributes.addFlashAttribute("successMessage", "성공적으로 저장하였습니다.");
        categoryService.save(dto.getName());
        return "redirect:/admin/categories";
    }
}
