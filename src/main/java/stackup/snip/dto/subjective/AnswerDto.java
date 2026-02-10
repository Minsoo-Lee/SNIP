package stackup.snip.dto.subjective;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@ToString
public class AnswerDto {
    private String category;
    private String question;
    private String content;
    private LocalDateTime createdAt;
}
