package stackup.snip.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stackup.snip.dto.question.SubjectiveDto;
import stackup.snip.entity.Subjective;
import stackup.snip.repository.jpa.SubjectiveJpaRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectiveService {

    private final SubjectiveJpaRepository subjectiveJpaRepository;
    private final NotionService notionService;

    @Transactional
    public void importFromNotion()  {
        List<SubjectiveDto> subjectiveDtos = notionService.getDatabasePages();
        for (SubjectiveDto subjectiveDto : subjectiveDtos) {
            Subjective subjective = new Subjective(subjectiveDto.getTitle(), subjectiveDto.getTag());
            subjectiveJpaRepository.save(subjective);
        }
    }
}
