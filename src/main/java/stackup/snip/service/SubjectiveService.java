package stackup.snip.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stackup.snip.dto.subjective.AdminSubjectiveDto;
import stackup.snip.dto.subjective.CategorySubjectiveDto;
import stackup.snip.dto.subjective.SubjectiveDetailDto;
import stackup.snip.dto.subjective.SubjectiveDto;
import stackup.snip.entity.Category;
import stackup.snip.entity.Subjective;
import stackup.snip.repository.jpa.AnswerJpaRepository;
import stackup.snip.repository.jpa.CategoryJpaRepository;
import stackup.snip.repository.jpa.SubjectiveJpaRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubjectiveService {

    private final SubjectiveJpaRepository subjectiveJpaRepository;
    private final AnswerJpaRepository answerJpaRepository;
    private final CategoryService categoryService;
    private final CategoryJpaRepository categoryJpaRepository;
    private final NotionService notionService;

    @Transactional
    public void importFromNotion() {
        List<SubjectiveDto> subjectiveDtos = notionService.getDatabasePages();
        for (SubjectiveDto subjectiveDto : subjectiveDtos) {
            String categoryName = subjectiveDto.getCategory();
            boolean categoryExists = categoryJpaRepository.existsByName(categoryName);
            Category category;
            if (categoryExists) {
                category = categoryJpaRepository.findCategoryByName(categoryName).getFirst();
            } else {
                category = new Category(categoryName);
                category.changeUpdatedAt();
                categoryJpaRepository.save(category);
            }
            Subjective subjective = new Subjective(subjectiveDto.getQuestion(), category);
            subjectiveJpaRepository.save(subjective);
        }
    }

    public Subjective getOneForTest() {
        return subjectiveJpaRepository.findById(1L).orElseThrow();
    }

    public SubjectiveDto getOneQuestion(Long memberId) {
        List<Long> subjectiveIds = answerJpaRepository.findAnsweredSubjectiveIdsByMemberId(memberId);

        Subjective subjective;

        if (subjectiveIds == null || subjectiveIds.isEmpty()) {
            List<Subjective> subjectiveList = subjectiveJpaRepository.findUnansweredRandomAll(memberId);

            System.out.println(subjectiveList);

            int randomIndex = (int) (Math.random() * subjectiveList.size());
            subjective = subjectiveList.get(randomIndex);
        } else {
            subjective = subjectiveJpaRepository.findRandomNotSelected(subjectiveIds).get();
        }

        return new SubjectiveDto(subjective);
    }

    public List<String> getAllCategories() {
        return categoryJpaRepository.findAll()
                .stream()
                .map(Category::getName)
                .toList();
    }

    public List<AdminSubjectiveDto> getAllSubjectives() {
        List<Subjective> all = subjectiveJpaRepository.findAll();
        List<AdminSubjectiveDto> subjectiveDtos = new ArrayList<>();
        for (Subjective subjective : all) {
            subjectiveDtos.add(
                    new AdminSubjectiveDto(subjective)
            );
        }
        return subjectiveDtos;
    }

    public List<AdminSubjectiveDto> getAllActiveSubjectives() {
        return subjectiveJpaRepository.findActiveSubjectives();
    }

    public List<AdminSubjectiveDto> getAllDeletedSubjectives() {
        return subjectiveJpaRepository.findDeletedSubjectives();
    }

    public List<CategorySubjectiveDto> getCategorySubjectDto(Long id) {
        List<Subjective> subjectives = subjectiveJpaRepository.getReferenceByCategory_Id(id);
        return subjectives.stream().map(s -> new CategorySubjectiveDto(s.getQuestion()))
                .toList();
    }

    public boolean ifExistsByContent(String content) {
        return subjectiveJpaRepository.existsByQuestion(content);
    }

    @Transactional
    public Subjective save(String categoryName, String content) {
        Category category = categoryJpaRepository.findCategoryByName(categoryName).getFirst();
        return subjectiveJpaRepository.save(new Subjective(
                content, category
        ));
    }

    public SubjectiveDetailDto getSubjectDetailById(Long id) {
        Subjective subjective = subjectiveJpaRepository.getReferenceById(id);
        log.info("categoryName = {}", subjective.getCategory().getName());
        return new SubjectiveDetailDto(
                subjective.getId(), subjective.getCategory().getName(), subjective.getQuestion()
        );
    }

    @Transactional
    public void changeContent(Long id, String content) {
        Subjective subjective = subjectiveJpaRepository.getReferenceById(id);
        subjective.changeQuestion(content);
    }

    @Transactional
    public void softDeleteSubjective(Long id) {
        Subjective subjective = subjectiveJpaRepository.getReferenceById(id);
        subjective.softDelete();
    }
}
