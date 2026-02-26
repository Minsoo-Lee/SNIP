package stackup.snip.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import stackup.snip.service.AnswerService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/growth")
public class GrowthController {

    private final AnswerService answerService;

    @GetMapping
    public String enterGrowth(
            @RequestAttribute("loginMemberId") Long memberId,
            Model model
    ) {
        // 이번 달 완료 개수
        int answerCount = answerService.countMonthlyAnswers(memberId);
        model.addAttribute("MonthlyCount", answerCount);

        // 지난 달 대비
        String monthDiffRate = answerService.getMonthDiffRate(memberId);
        model.addAttribute("monthDiffRate", monthDiffRate);

        // 최근 30일 평균 작성 길이
        String answerLengthAvg = answerService.countAnswersLengthAvg(memberId);
        model.addAttribute("recentAvgLength", answerLengthAvg);

        // 현재 진행 상태
        double progressRate = answerService.getProgressRate(memberId);
        model.addAttribute("progressRate", progressRate);

        return "sidebar/growth";
    }
}
