package stackup.snip.dto.subjective;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SubjectiveDetailDto {

    private Long id;
    private String categoryName;
    private String content;
}
