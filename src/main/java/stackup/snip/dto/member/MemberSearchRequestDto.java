package stackup.snip.dto.member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberSearchRequestDto {

    private String filter = "active";
    private String searchType;
    private String keyword;
}
