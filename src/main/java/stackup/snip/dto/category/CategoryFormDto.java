package stackup.snip.dto.category;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CategoryFormDto {

    private Long id;
    private String name;

    public CategoryFormDto() {
    }

    public CategoryFormDto(String name) {
        this.name = name;
    }

    public CategoryFormDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
