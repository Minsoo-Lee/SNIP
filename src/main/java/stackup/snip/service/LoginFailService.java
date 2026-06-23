package stackup.snip.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import stackup.snip.entity.Member;
import stackup.snip.repository.jpa.MemberJpaRepository;

@Service
@RequiredArgsConstructor
public class LoginFailService {

    private final MemberJpaRepository memberJpaRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void failLogin(Member member) {
        member.addLoginFailCount();
        if (member.getLoginFailCount() >= 5) {
            member.lockMember();
            member.initLoginFailCount();
        }
        memberJpaRepository.save(member);
    }
}
