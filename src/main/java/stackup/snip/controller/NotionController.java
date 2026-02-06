package stackup.snip.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import stackup.snip.dto.subjective.SubjectiveDto;
import stackup.snip.service.NotionService;

import java.util.List;

@RestController
public class NotionController {

    private final NotionService notionService;

    public NotionController(NotionService notionService) {
        this.notionService = notionService;
    }

    @GetMapping("/notion/update")
    public ResponseEntity<List<SubjectiveDto>> getPages() {
        List<SubjectiveDto> pages = notionService.getDatabasePages();
        return ResponseEntity.ok(pages);
    }
}
