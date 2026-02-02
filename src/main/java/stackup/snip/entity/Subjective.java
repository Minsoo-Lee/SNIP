package stackup.snip.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Subjective {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subjective_id")
    private Long id;

    private String question;
    private String category;

    public Subjective() {
    }

    public Subjective(String question, String category) {
        this.question = question;
        this.category = category;
    }
}
