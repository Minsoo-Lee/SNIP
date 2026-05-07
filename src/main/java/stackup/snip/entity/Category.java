package stackup.snip.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import stackup.snip.entity.base.TimeBaseEntity;

import java.util.List;

@Entity
@Getter
public class Category extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(unique = true)
    @Setter
    private String name;

    public Category() {
    }

    public Category(String name) {
        this.name = name;
        initUpdatedAt();
    }
}
