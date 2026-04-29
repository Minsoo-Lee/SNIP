package stackup.snip.entity;

import jakarta.persistence.*;
import lombok.Getter;
import stackup.snip.entity.base.TimeBaseEntity;

@Entity
@Getter
public class Subjective extends TimeBaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subjective_id")
    private Long id;

    private String question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    public Subjective() {
    }

    public Subjective(String question, Category category) {
        this.question = question;
        this.category = category;
        initUpdatedAt();
    }
}
