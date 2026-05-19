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
    private LocalDateTime deletedAt;

    public AdminSubjectiveDto(Subjective subjective) {
        this.id = subjective.getId();
        this.question = subjective.getQuestion();
        this.category = subjective.getCategory().getName();
        this.updatedAt = subjective.getUpdatedAt();
        this.deletedAt = subjective.getDeletedAt();
    }

    public AdminSubjectiveDto(Long id, String question, String category, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.question = question;
        this.category = category;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }
}
