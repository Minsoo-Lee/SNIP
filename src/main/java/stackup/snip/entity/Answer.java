package stackup.snip.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.apache.catalina.User;
import stackup.snip.entity.base.TimeBaseEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "subjective_id"})
        }
)
public class Answer extends TimeBaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subjective_id")
    private Subjective subjective;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String content;

    public Answer() {
    }

    public Answer(Subjective subjective, Member member, String content) {
        this.subjective = subjective;
        this.member = member;
        this.content = content;
    }
}
