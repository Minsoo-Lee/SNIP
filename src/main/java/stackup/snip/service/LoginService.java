package stackup.snip.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stackup.snip.dto.member.MemberLoginDto;
import stackup.snip.entity.Member;
import stackup.snip.exception.login.EmailNotExistException;
import stackup.snip.exception.login.LoginFailException;
import stackup.snip.exception.login.LoginPasswordNotMatchException;
import stackup.snip.repository.jpa.MemberJpaRepository;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberJpaRepository memberJpaRepository;

    @Transactional
    public Member register(Member member) {
        // 회원 아이디 중복 검증

        return memberJpaRepository.save(member);
    }

    @Transactional(readOnly = true)
    public Member login(String email, String password) {
        // email이 존재하는지 확인
        Member member = memberJpaRepository.findByEmail(email)
                .orElseThrow(EmailNotExistException::new);

        // email과 password가 일치하는지 확인
        if (!member.getPassword().equals(password)) {
            throw new LoginPasswordNotMatchException();
        }
        return member;
    }


//
//    @Transactional(readOnly = true)
//    public Member
}
