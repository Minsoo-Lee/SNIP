package stackup.snip.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import stackup.snip.dto.home.YesterdayDto;
import stackup.snip.entity.Answer;
import stackup.snip.entity.Subjective;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AnswerJpaRepository extends JpaRepository<Answer, Long> {

    @Query("select distinct a.subjective.id from Answer a where a.member.id = :memberId")
    List<Long> findAnsweredSubjectiveIdsByMemberId(@Param("memberId") Long memberId);

    @Query("select count(*) from Answer a where a.member.id = :memberId and a.createdAt >= :start and a.createdAt < :end")
    long countAnswersByMemberIdAndCreatedAt(
            @Param("memberId") Long memberId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("select count(*) from Answer a where a.member.id = :memberId")
    int countAnswersByMemberId(Long memberId);

    @Query("select new stackup.snip.dto.home.YesterdayDto(" +
            "a.subjective.category, a.subjective.question, a.content" +
            ") from Answer a where a.member.id = :memberId and a.createdAt >= :start and a.createdAt < :end"
    )
    Optional<YesterdayDto> getYesterdayDtoByMemberIdAndDate(
            @Param("memberId") Long memberId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
