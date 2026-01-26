package stackup.snip.service;

import org.aspectj.lang.annotation.RequiredTypes;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import stackup.snip.entity.Member;
import stackup.snip.exception.login.EmailDuplicateException;
import stackup.snip.exception.login.EmailNotExistException;
import stackup.snip.exception.login.LoginPasswordNotMatchException;
import stackup.snip.exception.login.NicknameDuplicateException;
import stackup.snip.repository.jpa.MemberJpaRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @BeforeEach
    public void beforeEach() {
        memberJpaRepository.save(new Member("email@snip.dev", "nickname", "1234", LocalDateTime.now(), 1));
    }

    @Test
    public void register_noEx() throws Exception{
        //given
        Member savedMember = memberService.register("email1@snip.dev", "nickname1", "1234", LocalDateTime.now(), 1);

        //when
        Member findMember = memberJpaRepository.findById(savedMember.getId()).get();

        //then
        assertThat(findMember).isEqualTo(savedMember);
        assertThat(findMember.getId()).isEqualTo(savedMember.getId());
        assertThat(findMember.getEmail()).isEqualTo(savedMember.getEmail());
        assertThat(findMember.getPassword()).isEqualTo(savedMember.getPassword());
        assertThat(findMember.getNickname()).isEqualTo(savedMember.getNickname());
    }

    @Test
    public void register_EmailDuplicateEx() throws Exception{
        //given
        assertThrows(EmailDuplicateException.class, () ->
            memberService.register("email@snip.dev", "nickname1", "1234", LocalDateTime.now(), 1));
    }

    @Test
    public void register_NicknameDuplicateEx() throws Exception{
        //given
        assertThrows(NicknameDuplicateException.class, () ->
                memberService.register("email1@snip.dev", "nickname", "1234", LocalDateTime.now(), 1));
    }

    @Test
    public void login_noEx() throws Exception{
        memberService.login("email@snip.dev", "1234");
    }

    @Test
    public void login_matchEx() throws Exception{
        assertThrows(LoginPasswordNotMatchException.class, () ->
                memberService.login("email@snip.dev", "12341"));
    }

    @Test
    public void login_noEmail() throws Exception{
        assertThrows(EmailNotExistException.class, () ->
                memberService.login("email1@snip.dev", "1234"));
    }
}