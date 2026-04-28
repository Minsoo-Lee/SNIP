package stackup.snip.entity;

import jakarta.persistence.*;
import lombok.Getter;
import stackup.snip.entity.base.TimeBaseEntity;

import java.time.LocalDateTime;

@Entity
@Getter
public class Subjective extends TimeBaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subjective_id")
    private Long id;

    private String question;
    private String category;

    private LocalDateTime updatedAt;

    public Subjective() {
    }

    public Subjective(String question, String category) {
        this.question = question;
        this.category = category;
        this.updatedAt = LocalDateTime.now();
    }

    //== 수정일자 업데이트 ==//
    public void changeUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
