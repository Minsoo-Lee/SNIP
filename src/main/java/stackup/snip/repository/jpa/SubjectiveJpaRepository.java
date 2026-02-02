package stackup.snip.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import stackup.snip.entity.Subjective;

@Repository
public interface SubjectiveJpaRepository extends JpaRepository<Subjective, Long> {

}
