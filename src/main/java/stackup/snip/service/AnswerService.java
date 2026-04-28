package stackup.snip.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stackup.snip.dto.home.YesterdayDto;
import stackup.snip.dto.subjective.AnswerDto;
import stackup.snip.dto.subjective.HistoryDto;
import stackup.snip.dto.subjective.MonthlyAnswersDto;
import stackup.snip.dto.subjective.MonthlyTrendDto;
import stackup.snip.entity.Answer;
import stackup.snip.entity.Member;
import stackup.snip.entity.Subjective;
import stackup.snip.exception.subjective.AnswerBlankException;
import stackup.snip.repository.jpa.AnswerJpaRepository;
import stackup.snip.repository.jpa.AnswerQuerydslRepository;
import stackup.snip.repository.jpa.MemberJpaRepository;
import stackup.snip.repository.jpa.SubjectiveJpaRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AnswerService {

    private final AnswerJpaRepository answerJpaRepository;
    private final AnswerQuerydslRepository answerQuerydslRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final SubjectiveJpaRepository subjectiveJpaRepository;

    @Transactional
    public void saveAnswer(Long memberId, String question, String content) {
        if (content.isBlank())
            throw new AnswerBlankException();
        Member member = memberJpaRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다. id=" + memberId));
        Subjective subjective = subjectiveJpaRepository.findByQuestion(question)
                .orElseThrow(() -> new IllegalArgumentException("해당 Question이 존재하지 않습니다."));
        Answer answer = new Answer(subjective, member, content);
        answerJpaRepository.save(answer);

        // 유저 streak 증가
        Member updateMember = memberJpaRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다. id=" + memberId));
        updateMember.addStreakOnce();
    }

    public boolean isCompletedToday(Long memberId) {
        LocalDate today = LocalDateTime.now().toLocalDate();
        long count = answerJpaRepository.countAnswersByMemberIdAndCreatedAt(memberId, today.atStartOfDay(), today.plusDays(1).atStartOfDay());

        return count > 0;
    }

    public boolean isCompletedYesterday(Long memberId) {
        LocalDate today = LocalDateTime.now().toLocalDate();
        long count = answerJpaRepository.countAnswersByMemberIdAndCreatedAt(memberId, today.minusDays(1).atStartOfDay(), today.atStartOfDay());

        return count > 0;
    }

    public int countTotalAnswers(Long memberId) {
        return answerJpaRepository.countAnswersByMemberId(memberId);
    }

    public YesterdayDto getYesterdayDto(Long memberId) {
        LocalDate today = LocalDateTime.now().toLocalDate();
        return answerJpaRepository.
                getYesterdayDtoByMemberIdAndDate(memberId, today.atStartOfDay(), today.plusDays(1).atStartOfDay())
                .orElse(null);
    }

    public List<HistoryDto> getQuestionsAnswersWithMemberId(Long memberId) {
        return answerJpaRepository.findAnswerByMemberId(memberId);
    }

    public AnswerDto getAnswerDetail(Long answerId) {
        Answer findAnswer = answerJpaRepository.findAnswerById(answerId);
        return new AnswerDto(
                findAnswer.getSubjective().getCategory(),
                findAnswer.getSubjective().getQuestion(),
                findAnswer.getContent(),
                findAnswer.getCreatedAt()
        );
    }

    public List<HistoryDto> getSearchResult(
            Long memberId, int days, String category, String question
    ) {
        List<Answer> findAnswers = answerQuerydslRepository.findByDateCategoryTitle(memberId, days, category, question);
        List<HistoryDto> result = new ArrayList<>();

        for (Answer findAnswer : findAnswers) {
            result.add(new HistoryDto(
                    findAnswer.getId(),
                    findAnswer.getSubjective().getCategory(),
                    findAnswer.getSubjective().getQuestion(),
                    findAnswer.getContent(),
                    findAnswer.getCreatedAt()
            ));
        }

        return result;
    }

    public int countMonthlyAnswers(Long memberId) {
        LocalDate now = LocalDate.now();
        LocalDateTime start = now.withDayOfMonth(1).atStartOfDay();
        LocalDateTime end = start.plusMonths(1);
        return answerJpaRepository.countMonthlyAnswersByMemberIdWithoutNull(memberId, start, end);
    }

    public String getMonthDiffRate(Long memberId) {
        int thisMonthCount = countMonthlyAnswers(memberId);

        LocalDate last = LocalDate.now().minusMonths(1);
        LocalDateTime start = last.withDayOfMonth(1).atStartOfDay();
        LocalDateTime end = start.plusMonths(1);

        Integer lastMonthCount = answerJpaRepository.countMonthlyAnswersByMemberIdWithoutNull(memberId, start, end);

        if (lastMonthCount == 0) {
            return "지난 달 기록이 없어 비교할 수 없어요😭";
        }

        // 단순 숫자계산 - 소수 2째 자리까지만 표시
        double monthDiffRate = (double) thisMonthCount / lastMonthCount;
        monthDiffRate = getTwoDigit(monthDiffRate * 100);

        return monthDiffRate + "%";
    }

    public String countAnswersLengthAvg(Long memberId) {
        int days = 30;

        LocalDateTime end = LocalDate.now().plusDays(1).atStartOfDay(); // 오늘 포함
        LocalDateTime start = end.minusDays(days);

        Double lenAvg = answerJpaRepository.findAverageAnswerLength(memberId, start, end);
        if (lenAvg == null) {
            return "아직 작성한 답안이 없어요😭";
        }
        lenAvg = getTwoDigit(lenAvg);

        return lenAvg + "자";
    }

    public double getProgressRate(Long memberId) {
        long allSubjectives = subjectiveJpaRepository.count();
        int completedAnswers = answerJpaRepository.countAnswersByMemberId(memberId);
        return getTwoDigit((double) completedAnswers / allSubjectives * 100);
    }

    private double getTwoDigit(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);

        return bd.doubleValue();
    }

    public MonthlyTrendDto getMonthlyTrend(long memberId, int currentMonths) {
        List<String> months = new ArrayList<>();
        for (int i = 0; i < currentMonths; i++) {
            String year = String.valueOf(LocalDateTime.now().minusMonths(i).getYear());
            log.info("year = " + year);
            String month = String.valueOf(LocalDateTime.now().minusMonths(i).getMonthValue());
            log.info("month = " + month);
            months.add(year + "-" + month);
        }

        for (String month : months) {
            System.out.println(month);
        }

        months = months.reversed();

        LocalDate now = LocalDate.now();

        LocalDateTime start = now
                .minusMonths(currentMonths)
                .withDayOfMonth(1)
                .atStartOfDay();

        LocalDateTime end = now
                .plusMonths(1)
                .withDayOfMonth(1)
                .atStartOfDay();

        MonthlyTrendDto monthlyTrend = new MonthlyTrendDto();
        MonthlyTrendDto monthlyTrendFromJpa = new MonthlyTrendDto();

        List<MonthlyAnswersDto> monthlyAnswersDtos = answerJpaRepository.countMonthlyAnswers(memberId, start, end);

        for (MonthlyAnswersDto monthlyAnswersDto : monthlyAnswersDtos) {
            monthlyTrendFromJpa.addMonth(monthlyAnswersDto.year() + "-" + monthlyAnswersDto.month());
            monthlyTrendFromJpa.addCount(Math.toIntExact(monthlyAnswersDto.count()));
        }

        for (int i = 0; i < months.size(); i++) {
            monthlyTrend.addMonth(months.get(i));
            int count = monthlyTrendFromJpa.ifContainsMonth(months.get(i)) ? monthlyTrendFromJpa.getCountFromMonth(months.get(i)) : 0;
            monthlyTrend.addCount(count);
//            if (monthlyTrendFromJpa.ifContainsMonth(months.get(i))) {
//                monthlyTrend.addCount(monthlyTrendFromJpa.getCountFromMonth(months.get(i)));
//            } else {
//
//                monthlyTrend.addCount(0);
//            }
        }
        return monthlyTrend;
    }
}
