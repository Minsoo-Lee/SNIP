package stackup.snip.dto.subjective;

import lombok.Getter;
import lombok.Setter;
import stackup.snip.entity.Subjective;

import java.time.LocalDateTime;

@Getter
@Setter
public class AdminSubjectiveDto {

    private Long id;
    private String question;
    private String category;
    private LocalDateTime updatedAt;

    public AdminSubjectiveDto(Subjective subjective) {
        this.id = subjective.getId();
        this.question = subjective.getQuestion();
        this.category = subjective.getCategory();
        this.updatedAt = subjective.getUpdatedAt();
    }
}
