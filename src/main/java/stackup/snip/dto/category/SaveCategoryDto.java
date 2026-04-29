package stackup.snip.dto.category;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SaveCategoryDto {

    private String name;

    public SaveCategoryDto(String name) {
        this.name = name;
    }
}
