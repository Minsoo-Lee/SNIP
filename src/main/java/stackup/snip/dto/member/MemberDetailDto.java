package stackup.snip.dto.member;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@Setter
public class MemberDetailDto {
    private Long id;
    private String nickname;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginDate;

    public MemberDetailDto() {
    }

    public MemberDetailDto(Long id, String nickname, String email, LocalDateTime createdAt, LocalDateTime lastLoginDate) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.createdAt = createdAt;
        this.lastLoginDate = lastLoginDate;
    }
}
