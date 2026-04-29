package stackup.snip.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import stackup.snip.entity.Category;

import java.util.List;

@Repository
public interface CategoryJpaRepository extends JpaRepository<Category, Long> {

    List<Category> findCategoryByName(String name);

    boolean existsByName(String name);
}
