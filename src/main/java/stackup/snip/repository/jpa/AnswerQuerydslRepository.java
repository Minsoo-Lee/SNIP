package stackup.snip.repository.jpa;

import ch.qos.logback.core.util.StringUtil;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import stackup.snip.dto.subjective.HistoryDto;
import stackup.snip.entity.Answer;
import stackup.snip.entity.QAnswer;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.util.StringUtils.*;
import static stackup.snip.entity.QAnswer.*;
import static stackup.snip.entity.QMember.*;

@Repository
@RequiredArgsConstructor
public class AnswerQuerydslRepository {

    private final EntityManager em;
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
        return hasText(category) ? answer.subjective.category.eq(category) : null;
    }

    private BooleanExpression questionLike(String title) {
        return hasText(title) ? answer.subjective.question.contains(title) : null;
    }
}
