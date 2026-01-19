package stackup.snip.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Getter
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @NotEmpty
    private String email;
    @NotEmpty
    private String nickname;
    @NotEmpty
    private String password;

    private LocalDateTime createdDate;
    private LocalDateTime lastLoginDate;

    private Integer loginStreak;

    //== 연관관계 ==//
    @OneToMany(mappedBy = "member")
    private List<LoginLog> loginLogs;

    //== 생성자 ==//
    public Member() {
    }

    public Member(String email, String nickname, String password, LocalDateTime dateTime, Integer loginStreak) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.createdDate = dateTime;
        this.lastLoginDate = dateTime;
        this.loginStreak = loginStreak;
    }
}
