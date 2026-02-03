package stackup.snip.entity;

import jakarta.persistence.*;
import stackup.snip.entity.base.TimeBaseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class LoginLog extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "login_log_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
}
