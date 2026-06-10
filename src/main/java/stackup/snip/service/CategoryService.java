package stackup.snip.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stackup.snip.dto.category.CategoryDetailDto;
import stackup.snip.dto.category.CategoryFormDto;
import stackup.snip.dto.category.CategoryListDto;
import stackup.snip.dto.category.CategorySearchRequestDto;
import stackup.snip.entity.Category;
import stackup.snip.entity.Subjective;
import stackup.snip.repository.jpa.CategoryJpaRepository;
import stackup.snip.repository.jpa.SubjectiveJpaRepository;
import stackup.snip.repository.querydsl.CategoryQueryDslRepository;
import stackup.snip.repository.querydsl.MemberQueryDslRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryJpaRepository categoryJpaRepository;
    private final SubjectiveJpaRepository subjectiveJpaRepository;
    private final CategoryQueryDslRepository categoryQueryDslRepository;

    public Category getOneByName(String name) {
        List<Category> categories = categoryJpaRepository.findCategoryByName(name);
        return categories.getFirst();
    }

    public CategoryFormDto getCategoryDetailDtoById(Long id) {
        return categoryJpaRepository.findCategoryDtoById(id);
    }

    public boolean ifExistsByName(String name) {
        return categoryJpaRepository.existsByName(name);
    }

    @Transactional
    public void save(Category category) {
        category.changeUpdatedAt();
        categoryJpaRepository.save(category);
    }

    @Transactional
    public Category save(String categoryName) {
        return categoryJpaRepository.save(new Category(categoryName));
    }

    public List<CategoryListDto> getAllCategories() {
        return categoryJpaRepository.findAll()
                .stream()
                .map(category -> new CategoryListDto(category.getId(), category.getName(), category.getUpdatedAt(), category.getDeletedAt()))
                .toList();
    }

    public List<CategoryListDto> getDeletedCategories() {
        return categoryJpaRepository.findDeletedCategories();
    }

    public List<CategoryListDto> getAllActiveCategories() {
        return categoryJpaRepository.findActiveCategories();
    }

    public List<String> getAllCategoryNames() {
        return categoryJpaRepository.findAll()
                .stream()
                .map(Category::getName)
                .toList();
    }

    @Transactional
    public void changeName(Long id, String name) {
        Category category = categoryJpaRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 카테고리입니다.")
        );
        category.setName(name);
    }

    @Transactional
    public void softDeleteCategory(Long id) {
        Category category = categoryJpaRepository.getReferenceById(id);
        category.softDelete();

        List<Subjective> subjectives = subjectiveJpaRepository.findByCategory(category);
        for (Subjective subjective : subjectives) {
            subjective.softDelete();
        }
    }

    public Page<CategoryListDto> getCategoriesByCondition(CategorySearchRequestDto dto) {
        Page<Category> page = categoryQueryDslRepository.findCategoriesByCondition(dto);

        List<CategoryListDto> content = page.getContent().stream().map(c -> new CategoryListDto(
                c.getId(),
                c.getName(),
                c.getUpdatedAt(),
                c.getDeletedAt()
        )).toList();

        return new PageImpl<>(content, PageRequest.of(dto.getPage(), dto.getSize()), page.getTotalElements());
    }
}
