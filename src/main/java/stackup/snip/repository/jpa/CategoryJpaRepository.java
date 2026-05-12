package stackup.snip.repository.jpa;

import org.hibernate.sql.results.graph.FetchList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import stackup.snip.dto.category.CategoryDetailDto;
import stackup.snip.dto.category.CategoryListDto;
import stackup.snip.entity.Category;

import java.util.List;

@Repository
public interface CategoryJpaRepository extends JpaRepository<Category, Long> {

    List<Category> findCategoryByName(String name);

    @Query("select new stackup.snip.dto.category.CategoryDetailDto(" +
            "c.id, c.name) " +
            "from Category c " +
            "where c.id = :id")
    CategoryDetailDto findCategoryDtoById(Long id);

    boolean existsByName(String name);

    @Query("select new stackup.snip.dto.category.CategoryListDto(" +
            "c.id, c.name, c.updatedAt, c.deletedAt) " +
            "from Category c " +
            "where c.deletedAt is not null")
    List<CategoryListDto> findDeletedCategories();

    @Query("select new stackup.snip.dto.category.CategoryListDto(" +
            "c.id, c.name, c.updatedAt, c.deletedAt) " +
            "from Category c " +
            "where c.deletedAt is null")
    List<CategoryListDto> findActiveCategories();
}
