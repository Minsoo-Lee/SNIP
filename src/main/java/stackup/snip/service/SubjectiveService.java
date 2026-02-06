package stackup.snip.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stackup.snip.dto.subjective.SubjectiveDto;
import stackup.snip.entity.Subjective;
import stackup.snip.repository.jpa.AnswerJpaRepository;
import stackup.snip.repository.jpa.SubjectiveJpaRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectiveService {

    private final SubjectiveJpaRepository subjectiveJpaRepository;
    private final AnswerJpaRepository answerJpaRepository;
    private final NotionService notionService;

    @Transactional
    public void importFromNotion() {
        List<SubjectiveDto> subjectiveDtos = notionService.getDatabasePages();
        for (SubjectiveDto subjectiveDto : subjectiveDtos) {
            Subjective subjective = new Subjective(subjectiveDto.getQuestion(), subjectiveDto.getCategory());
            subjectiveJpaRepository.save(subjective);
        }
    }

    @Transactional(readOnly = true)
    public Subjective getOneForTest() {
        return subjectiveJpaRepository.findById(1L).orElseThrow();
    }

    @Transactional(readOnly = true)
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
}
