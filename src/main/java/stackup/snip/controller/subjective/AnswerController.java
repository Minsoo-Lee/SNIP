package stackup.snip.controller.subjective;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import stackup.snip.service.AnswerService;

@Controller("/answer")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

    @PostMapping("/save")
    public void saveAnswer(
            @RequestParam String question,
            @RequestParam String content,
            @RequestAttribute("loginMemberId") Long memberId
    ) {
        // 답변 없으면 저장 안하고 경고 보내기?
        answerService.saveAnswer(memberId, question, content);

    }
}
