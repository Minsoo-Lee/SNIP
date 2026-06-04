package stackup.snip.repository.querydsl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import stackup.snip.entity.Answer;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.util.StringUtils.*;
import static stackup.snip.entity.QAnswer.*;

@Repository
@RequiredArgsConstructor
public class AnswerQuerydslRepository {

    private final JPAQueryFactory queryFactory;

    public List<Answer> findByDateCategoryTitle(
            Long memberId,
            int days,
            String category,
            String question
    ) {
        return queryFactory.selectFrom(answer)
                .where(
                        createdAtBetween(days),
                        categoryEq(category),
                        questionLike(question),
                        answer.member.id.eq(memberId)
                ).fetch();
    }

    private BooleanExpression createdAtBetween(int days) {
        if (days == 0)
            return null;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime before = now.minusDays(days);
        return answer.createdAt.between(before, now);
    }

    private BooleanExpression categoryEq(String category) {
        return hasText(category) ? answer.subjective.category.name.eq(category) : null;
    }

    private BooleanExpression questionLike(String title) {
        return hasText(title) ? answer.subjective.question.contains(title) : null;
    }
}
