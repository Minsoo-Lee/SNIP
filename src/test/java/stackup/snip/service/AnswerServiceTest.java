package stackup.snip.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AnswerServiceTest {

    @Autowired
    private AnswerService answerService;

    @Test
    void getQuestionsAnswers() {
        answerService.getQuestionsAnswersWithMemberId(1L);

    }
}