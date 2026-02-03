package stackup.snip.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stackup.snip.entity.Answer;
import stackup.snip.entity.Member;
import stackup.snip.entity.Subjective;
import stackup.snip.repository.jpa.AnswerJpaRepository;
import stackup.snip.repository.jpa.MemberJpaRepository;
import stackup.snip.repository.jpa.SubjectiveJpaRepository;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerJpaRepository answerJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final SubjectiveJpaRepository subjectiveJpaRepository;

    @Transactional
    public void saveAnswer(Long memberId, String question, String content) {
        Member member = memberJpaRepository.findById(memberId)
                .orElseThrow();
        Subjective subjective = subjectiveJpaRepository.findByQuestion(question).orElseThrow();
        Answer answer = new Answer(subjective, member, content);
        answerJpaRepository.save(answer);


    }
}
