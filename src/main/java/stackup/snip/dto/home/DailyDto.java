package stackup.snip.dto.home;

import lombok.Getter;
import lombok.Setter;

// 진행 상황 관련 - 연속 답변 / 총 답변
@Getter @Setter
public class DailyDto {

    private boolean isCompleted;
    private int answerStreak;
    private int totalAnswers;

    public DailyDto(boolean isCompleted, int answerStreak, int totalAnswers) {
        this.isCompleted = isCompleted;
        this.answerStreak = answerStreak;
        this.totalAnswers = totalAnswers;
    }
}
