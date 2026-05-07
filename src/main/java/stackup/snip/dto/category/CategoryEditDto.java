package stackup.snip.dto.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
public class CategoryEditDto {

    @Setter
    private Long id;
    private String name;

    public CategoryEditDto(String name) {
        this.name = name;
    }
}
