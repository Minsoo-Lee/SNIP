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
import static stackup.snip.entity.QMember.member;

@Repository
@RequiredArgsConstructor
public class CategoryQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    public Page<Category> findCategoriesByCondition(CategorySearchRequestDto dto) {

        BooleanExpression filterCondition = filterEq(dto.getFilter());
        BooleanExpression searchCondition = searchTypeEqAndKeywordLike(dto.getKeyword());

        List<Category> categories = queryFactory
                .selectFrom(category)
                .where(filterCondition, searchCondition)
                .orderBy(category.id.asc())
                .offset((long) dto.getPage() * dto.getSize())
                .limit(dto.getSize()).fetch();

        Long total = queryFactory
                .select(category.count())
                .from(category)
                .where(filterCondition, searchCondition)
                .fetchOne();

        return new PageImpl<>(categories, PageRequest.of(dto.getPage(), dto.getSize()),
                total == null ? 0 : total);
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
