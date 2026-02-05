package stackup.snip.dto.home;

import lombok.Getter;
import lombok.Setter;

// 어제의 기록 DTO
@Getter @Setter
public class YesterdayDto {

    private String category;
    private String question;
    private String answer;

    public YesterdayDto(String category, String question, String answer) {
        this.category = category;
        this.question = question;
        this.answer = answer;
    }
}
