package stackup.snip.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stackup.snip.dto.home.YesterdayDto;
import stackup.snip.entity.Answer;
import stackup.snip.entity.Member;
import stackup.snip.entity.Subjective;
import stackup.snip.exception.subjective.AnswerBlankException;
import stackup.snip.repository.jpa.AnswerJpaRepository;
import stackup.snip.repository.jpa.MemberJpaRepository;
import stackup.snip.repository.jpa.SubjectiveJpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerJpaRepository answerJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final SubjectiveJpaRepository subjectiveJpaRepository;

    @Transactional
    public void saveAnswer(Long memberId, String question, String content) {
        if (content.isBlank())
            throw new AnswerBlankException();
        Member member = memberJpaRepository.findById(memberId).orElseThrow();
        Subjective subjective = subjectiveJpaRepository.findByQuestion(question).orElseThrow();
        Answer answer = new Answer(subjective, member, content);
        answerJpaRepository.save(answer);

        // 유저 streak 증가
        Member updateMember = memberJpaRepository.findById(memberId).orElseThrow();
        updateMember.addStreakOnce();
    }

    @Transactional(readOnly = true)
    public boolean isCompletedToday(Long memberId) {
        LocalDate today = LocalDateTime.now().toLocalDate();
        long count = answerJpaRepository.countAnswersByMemberIdAndCreatedAt(memberId, today.atStartOfDay(), today.plusDays(1).atStartOfDay());

        return count > 0;
    }

    @Transactional(readOnly = true)
    public boolean isCompletedYesterday(Long memberId) {
        LocalDate today = LocalDateTime.now().toLocalDate();
        long count = answerJpaRepository.countAnswersByMemberIdAndCreatedAt(memberId, today.minusDays(1).atStartOfDay(), today.atStartOfDay());

        return count > 0;
    }

    @Transactional(readOnly = true)
    public int countTotalAnswers(Long memberId) {
        return answerJpaRepository.countAnswersByMemberId(memberId);
    }

    @Transactional(readOnly = true)
    public YesterdayDto getYesterdayDto(Long memberId) {
        LocalDate today = LocalDateTime.now().toLocalDate();
        return answerJpaRepository.
                getYesterdayDtoByMemberIdAndDate(memberId, today.atStartOfDay(), today.plusDays(1).atStartOfDay())
                .orElse(null);
    }
}
