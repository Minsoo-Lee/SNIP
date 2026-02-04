package stackup.snip.controller.subjective;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import stackup.snip.exception.subjective.AnswerBlankException;
import stackup.snip.service.AnswerService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/answer")
public class AnswerController {

    private final AnswerService answerService;

    @PostMapping("/save")
    public String saveAnswer(
            @RequestParam String question,
            @RequestParam String content,
            @RequestAttribute("loginMemberId") Long memberId,
            RedirectAttributes redirectAttributes) {

        boolean isCompleted;
        // 답변 없으면 저장 안하고 경고 보내기?
        try {
            answerService.saveAnswer(memberId, question, content);
            redirectAttributes.addFlashAttribute("completedToday", true);
        } catch (AnswerBlankException e) {
            redirectAttributes.addFlashAttribute("completedToday", false);
            redirectAttributes.addFlashAttribute("answerError", "답변을 입력해주세요.");
        }
        return "redirect:/";
    }
}
