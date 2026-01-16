package stackup.snip.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stackup.snip.entity.Member;
import stackup.snip.repository.jpa.MemberJpaRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberJpaRepository memberJpaRepository;

    @Transactional
    public Member save(Member member) {
        return memberJpaRepository.save(member);
    }
//
//    @Transactional(readOnly = true)
//    public Member
}
