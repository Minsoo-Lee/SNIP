package stackup.snip.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import stackup.snip.entity.base.TimeBaseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Getter
public class Member extends TimeBaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @NotEmpty
    private String email;
    @NotEmpty
    private String nickname;
    @NotEmpty
    private String password;

    private LocalDateTime lastLoginDate;

    private Integer loginStreak;
    private Integer answerStreak;

    //== 연관관계 ==//
    @OneToMany(mappedBy = "member")
    private List<LoginLog> loginLogs;

    //== 생성자 ==//
    public Member() {
    }

    public Member(String email, String nickname, String password, LocalDateTime dateTime) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.lastLoginDate = dateTime;
        this.loginStreak = 1;
        this.answerStreak = 0;
    }

    //== Streak ++ ==//
    public void addStreakOnce() {
        this.answerStreak++;
    }
}
