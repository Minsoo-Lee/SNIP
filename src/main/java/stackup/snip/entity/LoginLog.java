package stackup.snip.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class LoginLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "login_log_id")
    private Long id;

    private LocalDateTime loginDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
}
