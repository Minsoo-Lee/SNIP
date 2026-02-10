package stackup.snip.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.LifecycleState;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import stackup.snip.dto.subjective.AnswerDto;
import stackup.snip.dto.subjective.HistoryDto;
import stackup.snip.service.AnswerService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/history")
@RequiredArgsConstructor
@Slf4j
public class HistoryController {

    private final AnswerService answerService;

    @GetMapping
    public String enterHistory(
            Model model,
            @RequestAttribute("loginMemberId") Long memberId
    ) {

        List<HistoryDto> records = answerService.getQuestionsAnswersWithMemberId(memberId);

        for (HistoryDto record : records) {
            log.info("record = " + record);
        }

        model.addAttribute("records", records);
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
