package stackup.snip.notion;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class NotionClient {

    @Value("${notion.token}")
    private String notionApiKey;

    @Value("${notion.database_id}")
    private String databaseId;

    private final RestTemplate restTemplate;

    public NotionClient() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * 노션 데이터베이스 전체 조회
     */
    public String queryDatabase() {
        String url = "https://api.notion.com/v1/databases/" + databaseId + "/query";

        HttpHeaders headers = createHeaders();
        HttpEntity<String> entity = new HttpEntity<>("{}", headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class
        );

        return response.getBody();
    }

    /**
     * 공통 헤더 생성
     */
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + notionApiKey);
        headers.set("Notion-Version", "2022-06-28");
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}