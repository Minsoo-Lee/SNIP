package stackup.snip.controller.notion;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import stackup.snip.dto.notion.NotionDto;
import stackup.snip.service.NotionService;

import java.util.List;

@RestController
public class NotionController {

    private final NotionService notionService;

    public NotionController(NotionService notionService) {
        this.notionService = notionService;
    }

    @GetMapping("/notion/update")
    public ResponseEntity<List<NotionDto>> getPages() {
        List<NotionDto> pages = notionService.getDatabasePages();
        return ResponseEntity.ok(pages);
    }
}
