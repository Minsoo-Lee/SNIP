package stackup.snip.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import stackup.snip.entity.Answer;
import stackup.snip.entity.Subjective;

import java.util.List;
import java.util.Optional;

public interface AnswerJpaRepository extends JpaRepository<Answer, Long> {

    @Query("select distinct a.subjective.id from Answer a where a.member.id = :memberId")
    List<Long> findAnsweredSubjectiveIdsByMemberId(@Param("memberId") Long memberId);

//    @Query(value = """
//        SELECT s.*
//        FROM subjective s
//        LEFT JOIN answer a
//          ON s.id = a.subjective_id
//          AND a.member_id = :memberId
//        WHERE a.id IS NULL
//        ORDER BY RAND()
//        LIMIT 1
//        """, nativeQuery = true)
//    Optional<Subjective> findRandomUnanswered(@Param("memberId") Long memberId);
}
