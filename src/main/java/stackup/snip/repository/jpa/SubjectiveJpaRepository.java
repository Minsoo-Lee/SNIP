package stackup.snip.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import stackup.snip.dto.question.SubjectiveDto;
import stackup.snip.entity.Subjective;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectiveJpaRepository extends JpaRepository<Subjective, Long> {

    @Query(value = "SELECT * FROM subjective WHERE id NOT IN :selectedIds ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<Subjective> findRandomNotSelected(
            @Param("selectedIds") List<Long> selectedIds
    );

    @Query(value = """
        SELECT s.*
        FROM subjective as s
        LEFT JOIN answer as a
            ON s.subjective_id = a.subjective_id
            AND a.member_id = :memberId
        WHERE a.answer_id is NULL
        ORDER BY RAND()
        LIMIT 1
    """, nativeQuery = true)
    List<Subjective> findUnansweredRandomAll(Long memberId);

    Optional<Subjective> findByQuestion(String question);
}
