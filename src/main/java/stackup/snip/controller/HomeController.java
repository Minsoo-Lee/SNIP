package stackup.snip.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import stackup.snip.dto.question.SubjectiveDto;
import stackup.snip.service.SubjectiveService;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final SubjectiveService subjectiveService;

    // 질문 + 연속일수 + 총 문제수 띄우기
    @GetMapping("/")
    public String home(
            Model model,
            @RequestAttribute("loginMemberId") Long memberId
    ) {
        SubjectiveDto subjectiveDto = subjectiveService.getOneQuestion(memberId);

        model.addAttribute("category", subjectiveDto.getCategory());
        model.addAttribute("question", subjectiveDto.getQuestion());
        model.addAttribute("answer", "정답");
        return "home";
    }
}
