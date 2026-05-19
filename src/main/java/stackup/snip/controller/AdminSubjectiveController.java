package stackup.snip.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import stackup.snip.dto.subjective.AdminSubjectiveDto;
import stackup.snip.dto.subjective.SaveSubjective;
import stackup.snip.service.SubjectiveService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/subjectives")
@Slf4j
public class AdminSubjectiveController {

    private final SubjectiveService subjectiveService;

    @GetMapping
    public String subjectiveList(
            @RequestParam(defaultValue = "active") String filter,
            Model model) {

        List<AdminSubjectiveDto> subjectives = switch (filter) {
            case "active" -> subjectiveService.getAllActiveSubjectives();
            case "deleted" -> subjectiveService.getAllDeletedSubjectives();
            default -> subjectiveService.getAllSubjectives();
        };
        model.addAttribute("subjectives", subjectives);
        model.addAttribute("filter", filter);
        model.addAttribute("mode", "create");
        model.addAttribute("subjectiveForm", new SaveSubjective());

        return "sidebar/admin/subjectives";
    }
}
