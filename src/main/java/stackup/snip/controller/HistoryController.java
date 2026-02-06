package stackup.snip.controller;

import org.apache.catalina.LifecycleState;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import stackup.snip.dto.subjective.HistoryDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/history")
public class HistoryController {

    @GetMapping
    public String enterHistory(
            Model model
    ) {
        List<HistoryDto> records = new ArrayList<>();
        records.add(new HistoryDto(
                1L,
                "category1",
                "question1",
                "content1",
                LocalDateTime.now())
        );
        records.add(new HistoryDto(
                2L,
                "category2",
                "question2",
                "content2",
                LocalDateTime.now())
        );

        model.addAttribute("records", records);
        return "sidebar/history";
    }
}
