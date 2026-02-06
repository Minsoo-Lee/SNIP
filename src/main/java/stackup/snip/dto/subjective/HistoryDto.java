package stackup.snip.dto.subjective;

import lombok.AllArgsConstructor;
import lombok.Getter;


import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class HistoryDto {

    private Long id;
    private String category;
    private String question;
    private String content;
    private LocalDateTime createdAt;
}
