package stackup.snip.util;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import stackup.snip.entity.Answer;
import stackup.snip.entity.Category;
import stackup.snip.entity.Member;
import stackup.snip.repository.jpa.AnswerJpaRepository;
import stackup.snip.repository.jpa.CategoryJpaRepository;
import stackup.snip.repository.jpa.MemberJpaRepository;
import stackup.snip.repository.jpa.SubjectiveJpaRepository;
import stackup.snip.service.SubjectiveService;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Profile("dev") // 개발 환경에서만 실행
public class TestDataInit implements CommandLineRunner {

    private final MemberJpaRepository memberJpaRepository;
    private final CategoryJpaRepository categoryJpaRepository;
    private final SubjectiveService subjectiveService;

    @Override
    public void run(String... args) {
//        Member member1 = new Member(
//                "test@test.com",
//                "test",
//                "1234",
//                LocalDateTime.now()
//        );
//        Member admin = new Member(
//                "admin@admin.dev",
//                "admin",
//                "1234",
//                LocalDateTime.now()
//        );
//        memberJpaRepository.save(member1);
//        memberJpaRepository.save(admin);
//        subjectiveService.importFromNotion();

        Member admin = new Member(
                "admin@admin.dev",
                "admin",
                "1234",
                LocalDateTime.now()
        );
        memberJpaRepository.save(admin);
        for (int i = 0; i < 100; i++) {
            Member member = new Member(
                "test" + i + "@test.com",
                "test" + i,
                "1234",
                LocalDateTime.now()
            );
            memberJpaRepository.save(member);
        }
        for (int i = 0; i < 100; i++) {
            Category member = new Category(
                    "test" + i
            );
            categoryJpaRepository.save(member);
        }
        subjectiveService.importFromNotion();
    }
}


