package stackup.snip.dto.member;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class MemberSaveDto {
    private String email;
    private String nickname;
    private String password;
    private String confirmPassword;
}
