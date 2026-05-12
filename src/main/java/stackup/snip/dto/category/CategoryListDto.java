package stackup.snip.dto.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class CategoryListDto {

    private Long id;
    private String name;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
