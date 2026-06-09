package stackup.snip.dto.category;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategorySearchRequestDto {

    private String filter = "active";
    private String keyword;

    private int page = 0;
    private int size = 10;

    public CategorySearchRequestDto() {

    }

    public CategorySearchRequestDto(String filter) {
        this.filter = filter;
    }
}
