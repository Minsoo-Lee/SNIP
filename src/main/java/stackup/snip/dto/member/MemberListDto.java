package stackup.snip.dto.member;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@ToString
public class MemberListDto {
    private Long id;
    private String nickname;
    private String email;
    private LocalDateTime lastLoginDate;
    private LocalDateTime deletedAt;

    public MemberListDto(Long id, String nickname, String email, LocalDateTime lastLoginDate, LocalDateTime deletedAt) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.lastLoginDate = lastLoginDate;
        this.deletedAt = deletedAt;
    }
}
