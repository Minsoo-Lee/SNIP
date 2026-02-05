package stackup.snip.dto.question;

import lombok.Getter;
import lombok.Setter;
import stackup.snip.entity.Subjective;

@Getter @Setter
public class SubjectiveDto {
    private String question;
    private String category;

    public SubjectiveDto() {}

    public SubjectiveDto(Subjective subjective) {
        this.question = subjective.getQuestion();
        this.category = subjective.getCategory();
    }

    public SubjectiveDto(String question, String category) {
        this.question = question;
        this.category = category;
    }
}
