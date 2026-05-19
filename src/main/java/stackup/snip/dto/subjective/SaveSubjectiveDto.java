package stackup.snip.dto.subjective;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaveSubjectiveDto {

    private String categoryName;
    private String content;
}
