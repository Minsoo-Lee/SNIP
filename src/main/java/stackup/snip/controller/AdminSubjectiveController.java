package stackup.snip.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import stackup.snip.dto.subjective.AdminSubjectiveDto;
import stackup.snip.service.SubjectiveService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/subjectives")
@Slf4j
public class AdminSubjectiveController {

    private final SubjectiveService subjectiveService;

    @GetMapping
    public String subjectiveList(Model model) {
        List<AdminSubjectiveDto> subjectives = subjectiveService.getAllSubjectives();
        model.addAttribute("subjectives", subjectives);
        return "sidebar/admin/subjectives";
    }
}
