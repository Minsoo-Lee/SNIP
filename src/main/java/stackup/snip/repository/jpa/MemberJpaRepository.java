package stackup.snip.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import stackup.snip.entity.Member;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

}
