package stackup.snip.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stackup.snip.dto.subjective.AdminSubjectiveDto;
import stackup.snip.dto.subjective.SubjectiveDto;
import stackup.snip.entity.Category;
import stackup.snip.entity.Subjective;
import stackup.snip.repository.jpa.AnswerJpaRepository;
import stackup.snip.repository.jpa.CategoryJpaRepository;
import stackup.snip.repository.jpa.SubjectiveJpaRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubjectiveService {

    private final SubjectiveJpaRepository subjectiveJpaRepository;
    private final AnswerJpaRepository answerJpaRepository;
    private final CategoryService categoryService;
    private final NotionService notionService;

    @Transactional
    public void importFromNotion() {
        List<SubjectiveDto> subjectiveDtos = notionService.getDatabasePages();
        for (SubjectiveDto subjectiveDto : subjectiveDtos) {
            String categoryName = subjectiveDto.getCategory();
            boolean categoryExists = categoryService.ifExistsByName(categoryName);
            Category category;
            if (categoryExists) {
                category = categoryService.getOne(categoryName);
            } else {
                category = new Category(categoryName);
                categoryService.save(category);
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
        return subjectiveJpaRepository.findAllCategories().stream().map(Category::getName).toList();
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
}
