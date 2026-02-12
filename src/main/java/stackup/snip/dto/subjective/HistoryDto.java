package stackup.snip.dto.subjective;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;


import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@ToString
public class HistoryDto {

    private Long id;
    private String category;
    private String question;
    private String content;
    private LocalDateTime createdAt;
}
