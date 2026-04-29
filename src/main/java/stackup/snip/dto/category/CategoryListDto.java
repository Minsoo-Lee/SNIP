package stackup.snip.dto.category;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class CategoryListDto {

    private Long id;
    private String name;
    private LocalDateTime updatedAt;

    public CategoryListDto(Long id, String name, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.updatedAt = updatedAt;
    }
}
