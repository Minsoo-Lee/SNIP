package stackup.snip.repository.querydsl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
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

    public List<Member> findMembersByCondition(MemberSearchRequestDto dto) {
        return queryFactory
                .selectFrom(member)
                .where(
                        filterEq(dto.getFilter()),
                        searchTypeEqAndKeywordLike(dto.getSearchType(), dto.getKeyword())
                ).fetch();
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
