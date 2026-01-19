package stackup.snip.dto.member;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberLoginDto {

    private String email;
    private String password;
}
