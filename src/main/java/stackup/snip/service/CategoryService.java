package stackup.snip.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import stackup.snip.dto.category.CategoryDetailDto;
import stackup.snip.dto.category.CategoryListDto;
import stackup.snip.entity.Category;
import stackup.snip.repository.jpa.CategoryJpaRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryJpaRepository categoryJpaRepository;

    public Category getOneByName(String name) {
        List<Category> categories = categoryJpaRepository.findCategoryByName(name);
        return categories.getFirst();
    }

    public CategoryDetailDto getCategoryDetailDtoById(Long id) {
        return categoryJpaRepository.findCategoryDtoById(id);
    }

    public boolean ifExistsByName(String name) {
        return categoryJpaRepository.existsByName(name);
    }

    public void save(Category category) {
        category.changeUpdatedAt();
        categoryJpaRepository.save(category);
    }

    public void save(String categoryName) {
        categoryJpaRepository.save(new Category(categoryName));
    }

    public List<CategoryListDto> getAllCategories() {
        return categoryJpaRepository.findAll()
                .stream()
                .map(category -> new CategoryListDto(category.getId(), category.getName(), category.getUpdatedAt()))
                .toList();
    }

    public List<String> getAllCategoryNames() {
        return categoryJpaRepository.findAll()
                .stream()
                .map(Category::getName)
                .toList();
    }

    public void changeName(Long id, String name) {
        Category category = categoryJpaRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 카테고리입니다.")
        );
        category.setName(name);
    }
}
