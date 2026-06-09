package stackup.snip.repository.querydsl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import stackup.snip.dto.category.CategorySearchRequestDto;
import stackup.snip.dto.member.MemberSearchRequestDto;
import stackup.snip.entity.Category;
import stackup.snip.entity.Member;

import java.util.List;

import static org.springframework.util.StringUtils.hasText;
import static stackup.snip.entity.QCategory.category;

@Repository
@RequiredArgsConstructor
public class CategoryQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    public List<Category> findCategoriesByCondition(CategorySearchRequestDto dto) {
        return queryFactory
                .selectFrom(category)
                .where(
                        filterEq(dto.getFilter()),
                        searchTypeEqAndKeywordLike(dto.getKeyword())
                ).fetch();
    }

    private BooleanExpression filterEq(String filter) {
        return switch (filter) {
            case("active") -> category.deletedAt.isNull();
            case ("deleted") -> category.deletedAt.isNotNull();
            default -> null;
        };
    }

    private BooleanExpression searchTypeEqAndKeywordLike(String keyword) {
        return hasText(keyword) ? category.name.contains(keyword) : null;
    }
}
