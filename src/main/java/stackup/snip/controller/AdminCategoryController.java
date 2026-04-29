package stackup.snip.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
        return "sidebar/admin/categories";
    }

    @PostMapping
    public String saveCategory(
            SaveCategoryDto dto,
            Model model
    ) {

    }
}
