package stackup.snip.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestAttribute;
import stackup.snip.entity.Member;
import stackup.snip.exception.login.EmailDuplicateException;
import stackup.snip.exception.login.EmailNotExistException;
import stackup.snip.exception.login.LoginPasswordNotMatchException;
import stackup.snip.exception.login.NicknameDuplicateException;
import stackup.snip.repository.jpa.MemberJpaRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberJpaRepository memberJpaRepository;

    @Transactional
    public Member register(String email, String nickname, String password, LocalDateTime registeredTime, int streak) {
        // 회원 아이디 중복 검증
        if (memberJpaRepository.existsByEmail(email)) {
            throw new EmailDuplicateException();
        }

        // 회원 닉네임 중복 검증
        if (memberJpaRepository.existsByNickname(nickname)) {
            throw new NicknameDuplicateException();
        }

        return memberJpaRepository.save(new Member(email, nickname, password, registeredTime));
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

    @Transactional(readOnly = true)
    public int getAnswerStreak(Long memberId) {
        return memberJpaRepository.getAnswerStreakByMemberId(memberId);
    }
}
