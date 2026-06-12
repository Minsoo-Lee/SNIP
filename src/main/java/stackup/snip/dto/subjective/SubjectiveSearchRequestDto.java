package stackup.snip.dto.subjective;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubjectiveSearchRequestDto {

    private String filter = "active";
    private String searchType;
    private String keyword;

    private int page = 0;
    private int size = 10;

    public SubjectiveSearchRequestDto() {
    }

    public SubjectiveSearchRequestDto(String filter, String searchType, String keyword) {
        this.filter = filter;
        this.searchType = searchType;
        this.keyword = keyword;
    }
}
