package stackup.snip.dto.member;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MemberFormDto {

    private Long id;
    private String email;
    private String nickname;

    private String password;
    private String confirmPassword;

    private LocalDateTime createdAt;
    private LocalDateTime lastLoginDate;

    public MemberFormDto() {
    }

    public MemberFormDto(String nickname) {
        this.nickname = nickname;
    }

    public MemberFormDto(Long id, String email, String nickname, LocalDateTime createdAt, LocalDateTime lastLoginDate) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.createdAt = createdAt;
        this.lastLoginDate = lastLoginDate;
    }
}
