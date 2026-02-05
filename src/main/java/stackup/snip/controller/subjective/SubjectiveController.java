package stackup.snip.controller.subjective;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import stackup.snip.service.SubjectiveService;

@RestController
@RequiredArgsConstructor
public class SubjectiveController {

    private final SubjectiveService subjectiveService;

    // 초반 DB 쌓기용
    @GetMapping("/subjective/add")
    public void addSubjectives() {
        subjectiveService.importFromNotion();
    }
}
