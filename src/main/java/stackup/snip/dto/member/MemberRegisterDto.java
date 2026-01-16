package stackup.snip.dto.member;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberRegisterDto {

    private String email;
    private String nickname;
    private String password;
}
