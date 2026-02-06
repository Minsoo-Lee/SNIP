package stackup.snip.service;

import org.springframework.stereotype.Service;
import stackup.snip.dto.subjective.SubjectiveDto;
import stackup.snip.notion.NotionClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotionService {

    private final NotionClient notionClient;
    private final ObjectMapper objectMapper;

    public NotionService(NotionClient notionClient) {
        this.notionClient = notionClient;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * 데이터베이스의 모든 페이지 조회 (제목, 태그)
     */
    public List<SubjectiveDto> getDatabasePages() {
        String jsonResponse = notionClient.queryDatabase();

        // 디버깅: 실제 응답 출력
        System.out.println("=== Notion API Response ===");
        System.out.println(jsonResponse);
        System.out.println("===========================");

        return parseNotionResponse(jsonResponse);
    }

    /**
     * 노션 응답 파싱
     */
    private List<SubjectiveDto> parseNotionResponse(String jsonResponse) {
        List<SubjectiveDto> pages = new ArrayList<>();

        try {
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode results = root.get("results");

            for (JsonNode result : results) {
                // 디버깅: 각 페이지의 properties 출력
                System.out.println("=== Page Properties ===");
                System.out.println(result.get("properties").toPrettyString());
                System.out.println("=======================");

                SubjectiveDto page = new SubjectiveDto();
//                page.setId(result.get("id").asText());

                JsonNode properties = result.get("properties");

                // 제목 추출
                page.setQuestion(extractTitle(properties));

                // 태그 추출 (첫 번째 태그만)
                page.setCategory(extractTag(properties));

                pages.add(page);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Notion response", e);
        }

        return pages;
    }

    /**
     * 제목 추출
     */
    private String extractTitle(JsonNode properties) {
        JsonNode titleProperty = properties.get("이름");  // 한글 컬럼명
        if (titleProperty != null && titleProperty.has("title")) {
            JsonNode titleArray = titleProperty.get("title");
            if (titleArray.isArray() && !titleArray.isEmpty()) {
                return titleArray.get(0).get("plain_text").asText();
            }
        }
        return "";
    }

    /**
     * 태그 추출 (첫 번째 태그만 반환)
     */
    private String extractTag(JsonNode properties) {
        JsonNode tagsProperty = properties.get("태그");  // 한글 컬럼명

        if (tagsProperty != null && tagsProperty.has("multi_select")) {
            JsonNode tagsArray = tagsProperty.get("multi_select");
            if (tagsArray.isArray() && !tagsArray.isEmpty()) {
                return tagsArray.get(0).get("name").asText();
            }
        }

        return null;
    }
}