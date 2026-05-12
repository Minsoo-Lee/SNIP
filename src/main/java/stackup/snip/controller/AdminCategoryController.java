package stackup.snip.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import stackup.snip.dto.category.CategoryDetailDto;
import stackup.snip.dto.category.CategoryEditDto;
import stackup.snip.dto.category.CategoryListDto;
import stackup.snip.dto.category.SaveCategoryDto;
import stackup.snip.service.CategoryService;

import java.util.List;

@Controller
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public String categoryList(
            Model model,
            @RequestParam(defaultValue = "active") String filter
    ) {
        List<CategoryListDto> categories = switch (filter) {
            case "deleted" -> categoryService.getDeletedCategories();
            case "all" -> categoryService.getAllCategories();
            default -> categoryService.getAllActiveCategories();
        };
        model.addAttribute("categories", categories);
        model.addAttribute("categoryForm", new SaveCategoryDto());
        model.addAttribute("filter", filter);
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

    @GetMapping("/{id}")
    public String categoryDetail(
            @PathVariable Long id,
            Model model
    ) {
        CategoryDetailDto categoryDetailDto = categoryService.getCategoryDetailDtoById(id);
        model.addAttribute("category", categoryDetailDto);
        model.addAttribute("selectedId", id);
        model.addAttribute("categories", categoryService.getAllActiveCategories());
        model.addAttribute("filter", "active");
        return "sidebar/admin/categories";
    }

    @PostMapping("/{id}")
    public String categoryEdit(
            @PathVariable Long id,
            @ModelAttribute("category") CategoryEditDto dto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (categoryService.ifExistsByName(dto.getName())) {
            result.rejectValue("name", null, "[" + dto.getName() + "]: 중복된 카테고리입니다.");
        }
        if (result.hasErrors()) {
            dto.setId(id);
            model.addAttribute("category", dto);
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("selectedId", id);
            model.addAttribute("currentTab", "categories");
            return "sidebar/admin/categories";
        }
        categoryService.changeName(id, dto.getName());
        redirectAttributes.addFlashAttribute("successMessage", "수정이 완료되었습니다.");
        return "redirect:/admin/categories/{id}";
    }

    /**
     * 카테고리 지우면 문제까지 지워버리자...
     * 그 전에 어떤 문제가 지워질지 안내창 띄우는게 더 나을지도?
     */
    @PostMapping("/{id}/delete")
    public String categoryDelete(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        categoryService.softDeleteCategory(id);
        redirectAttributes.addFlashAttribute("successMessage", "수정이 완료되었습니다.");
        return "redirect:/admin/categories/{id}";
    }
}
