package stackup.snip.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.LifecycleState;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import stackup.snip.dto.subjective.AnswerDto;
import stackup.snip.dto.subjective.HistoryDto;
import stackup.snip.service.AnswerService;
import stackup.snip.service.SubjectiveService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/history")
@RequiredArgsConstructor
@Slf4j
public class HistoryController {

    private final AnswerService answerService;
    private final SubjectiveService subjectiveService;

    @GetMapping
    public String enterHistory(
            Model model,
            @RequestAttribute("loginMemberId") Long memberId,
            @RequestParam(required = false, defaultValue = "0") int period,
            @RequestParam(required = false, defaultValue = "") String category,
            @RequestParam(required = false, defaultValue = "") String keyword
    ) {
        List<String> allCategories = subjectiveService.getAllCategories();
        List<HistoryDto> records = answerService.getSearchResult(memberId, period, category, keyword);
//        List<HistoryDto> records = answerService.getQuestionsAnswersWithMemberId(memberId);

        model.addAttribute("records", records);
        model.addAttribute("categories", allCategories);

        return "sidebar/history";
    }

    @GetMapping("/{id}")
    public String historyDetail(
            Model model,
            @PathVariable("id") Long answerId
    ) {
        AnswerDto answerDetail = answerService.getAnswerDetail(answerId);
        log.info("category raw = >{}<", answerDetail.getCategory());
        log.info("length = {}", answerDetail.getCategory().length());
        model.addAttribute("answerDetail", answerDetail);
        return "sidebar/answerDetail";
    }
}
