package stackup.snip.repository.querydsl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import stackup.snip.dto.subjective.SubjectiveSearchRequestDto;
import stackup.snip.entity.QSubjective;
import stackup.snip.entity.Subjective;

import java.util.List;

import static org.springframework.util.StringUtils.hasText;
import static stackup.snip.entity.QMember.member;
import static stackup.snip.entity.QSubjective.subjective;

@Repository
@RequiredArgsConstructor
public class SubjectiveQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    public Page<Subjective> findSubjectivesByCondition(SubjectiveSearchRequestDto dto) {

        BooleanExpression filterCondition = filterEq(dto.getFilter());
        BooleanExpression searchCondition = searchTypeEqAndKeywordLike(dto.getSearchType(), dto.getKeyword());

        List<Subjective> subjectives = queryFactory
                .selectFrom(subjective)
                .where(filterCondition, searchCondition)
                .orderBy(subjective.id.asc())
                .offset((long) dto.getPage() * dto.getSize())
                .limit(dto.getSize())
                .fetch();

        Long total = queryFactory
                .select(subjective.count())
                .from(subjective)
                .where(filterCondition, searchCondition)
                .fetchOne();

        return new PageImpl<>(subjectives, PageRequest.of(dto.getPage(), dto.getSize()),
                total == null ? 0 : total);
    }

    private BooleanExpression filterEq(String filter) {
        return switch (filter) {
            case("active") -> subjective.deletedAt.isNull();
            case ("deleted") -> subjective.deletedAt.isNotNull();
            default -> null;
        };
    }

    private BooleanExpression searchTypeEqAndKeywordLike(String searchType, String keyword) {
        if (!hasText(searchType) || !hasText(keyword)) {
            return null;
        }
        return switch (searchType) {
            case ("category") -> subjective.category.name.contains(keyword);
            default -> subjective.question.contains(keyword);
        };
    }
}
