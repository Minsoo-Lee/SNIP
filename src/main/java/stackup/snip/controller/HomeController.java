package stackup.snip.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import stackup.snip.dto.home.DailyDto;
import stackup.snip.dto.home.YesterdayDto;
import stackup.snip.dto.subjective.SubjectiveDto;
import stackup.snip.service.AnswerService;
import stackup.snip.service.MemberService;
import stackup.snip.service.SubjectiveService;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final SubjectiveService subjectiveService;
    private final AnswerService answerService;
    private final MemberService memberService;

    // 질문 + 연속일수 + 총 문제수 띄우기
    @GetMapping("/")
    public String home(
            Model model,
            @RequestAttribute("loginMemberId") Long memberId
    ) {
        // 어제의 기록 탭
        boolean completedYesterday = answerService.isCompletedYesterday(memberId);
        model.addAttribute("completedYesterday", completedYesterday);

        YesterdayDto yesterday;

        log.info("yesterday = "+ completedYesterday);

        if (completedYesterday) {
            yesterday = answerService.getYesterdayDto(memberId);
        } else {
            yesterday = new YesterdayDto("", "", "");
        }
        model.addAttribute("yesterday", yesterday);

        // 진행 상황 섹션
        boolean completedToday = answerService.isCompletedToday(memberId);
        DailyDto daily = new DailyDto(
                completedToday,
                memberService.getAnswerStreak(memberId),
                answerService.countTotalAnswers(memberId)
        );
        model.addAttribute("daily", daily);

        // 오늘의 문제
        SubjectiveDto subjectiveDto;
        if (!completedToday) {
            subjectiveDto = subjectiveService.getOneQuestion(memberId);
        } else {
            subjectiveDto = new SubjectiveDto("", "");
        }
        model.addAttribute("subjectiveDto", subjectiveDto);
        return "home";
    }

    @PostMapping("/answer/save")
    public String saveAnswer(
            @RequestParam String question,
            @RequestParam String content,
            @RequestAttribute("loginMemberId") Long memberId,
            RedirectAttributes redirectAttributes) {
        answerService.saveAnswer(memberId, question, content);

        return "redirect:/";
    }
}
