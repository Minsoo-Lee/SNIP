package stackup.snip.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stackup.snip.dto.member.MemberFormDto;
import stackup.snip.dto.member.MemberSaveDto;
import stackup.snip.dto.member.MemberListDto;
import stackup.snip.entity.Member;
import stackup.snip.exception.login.EmailDuplicateException;
import stackup.snip.exception.login.EmailNotExistException;
import stackup.snip.exception.login.LoginPasswordNotMatchException;
import stackup.snip.exception.login.NicknameDuplicateException;
import stackup.snip.repository.jpa.MemberJpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
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

    public int getAnswerStreak(Long memberId) {
        return memberJpaRepository.getAnswerStreakByMemberId(memberId);
    }

    public boolean checkMatchPassword(Long memberId, String password) {
        String checkPassword = memberJpaRepository.findPasswordById(memberId);
        return Objects.equals(checkPassword, password);
    }

    @Transactional
    public void changeNickname(Long memberId, String newNickname) {
        Member member = memberJpaRepository.findById(memberId).orElseThrow();
        member.updateNickname(newNickname);
    }

    @Transactional
    public void changePassword(Long memberId, String newPassword) {
        Member member = memberJpaRepository.findById(memberId).orElseThrow();
        member.updatePassword(newPassword);
    }

    public boolean checkAdminPassword(Long memberId, String password) {
        Member member = memberJpaRepository.getReferenceById(memberId);
        return password.equals(member.getPassword());
    }

    public List<MemberListDto> getAllMembers() {
        List<Member> members = memberJpaRepository.findAll();
        return members.stream().map(m -> new MemberListDto(
                m.getId(),
                m.getNickname(),
                m.getEmail(),
                m.getLastLoginDate())
        ).toList();
    }

    @Transactional
    public void save(MemberFormDto memberForm) {
        memberJpaRepository.save(new Member(
                memberForm.getEmail(),
                memberForm.getNickname(),
                memberForm.getPassword(),
                LocalDateTime.now()
        ));
    }

    public MemberFormDto getMemberById(Long id) {
        Member member = memberJpaRepository.findById(id).orElseThrow();
        log.info("[service] member = " + member);
        return new MemberFormDto(
                member.getId(),
                member.getEmail(),
                member.getNickname(),
                member.getCreatedAt(),
                member.getLastLoginDate()
        );
    }
}
