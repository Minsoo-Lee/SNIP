package stackup.snip.repository.querydsl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import stackup.snip.dto.member.MemberSearchRequestDto;
import stackup.snip.entity.Member;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.util.StringUtils.*;
import static stackup.snip.entity.QMember.*;

@Repository
@RequiredArgsConstructor
public class MemberQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    public Page<Member> findMembersByCondition(MemberSearchRequestDto dto) {

        BooleanExpression filterCondition = filterEq(dto.getFilter());
        BooleanExpression searchCondition = searchTypeEqAndKeywordLike(dto.getSearchType(), dto.getKeyword());

        List<Member> members = queryFactory
                .selectFrom(member)
                .where(filterCondition, searchCondition)
                .orderBy(member.id.desc())
                .offset((long) dto.getPage() * dto.getSize())
                .limit(dto.getSize()).fetch();

        Long total = queryFactory
                .select(member.count())
                .from(member)
                .where(filterCondition, searchCondition)
                .fetchOne();

        return new PageImpl<>(members, PageRequest.of(dto.getPage(), dto.getSize()),
                total == null ? 0 : total);
    }

    private BooleanExpression filterEq(String filter) {
        return switch (filter) {
            case("active") -> member.deletedAt.isNull();
            case ("deleted") -> member.deletedAt.isNotNull();
            default -> null;
        };
    }

    private BooleanExpression searchTypeEqAndKeywordLike(String searchType, String keyword) {
        if (!hasText(searchType) || !hasText(keyword)) {
            return null;
        }
        return switch (searchType) {
            case ("nickname") -> member.nickname.contains(keyword);
            default -> member.email.contains(keyword);
        };
    }
}
