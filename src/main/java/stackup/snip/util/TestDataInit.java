package stackup.snip.util;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import stackup.snip.entity.Member;
import stackup.snip.repository.jpa.MemberJpaRepository;
import stackup.snip.repository.jpa.SubjectiveJpaRepository;
import stackup.snip.service.SubjectiveService;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Profile("dev") // 개발 환경에서만 실행
public class TestDataInit implements CommandLineRunner {

    private final MemberJpaRepository memberJpaRepository;
    private final SubjectiveService subjectiveService;

    @Override
    public void run(String... args) {
        memberJpaRepository.save(
                new Member(
                        "test@test.com",
                        "test",
                        "1234",
                        LocalDateTime.now()
                )
        );
        memberJpaRepository.save(
                new Member(
                        "admin@admin.dev",
                        "test",
                        "1234",
                        LocalDateTime.now()
                )
        );
        subjectiveService.importFromNotion();
    }
}


