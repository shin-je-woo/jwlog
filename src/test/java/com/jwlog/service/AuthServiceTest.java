package com.jwlog.service;

import com.jwlog.domain.User;
import com.jwlog.exception.AlreadyExistsEmailException;
import com.jwlog.repository.UserRepository;
import com.jwlog.request.Signup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공")
    void test1() throws Exception {
        // given
        Signup signup = Signup.builder()
                .name("신제우")
                .password("1234")
                .email("shinjw@naver.com")
                .build();

        // when
        authService.signup(signup);

        // then
        assertEquals(1, userRepository.count());

        User user = userRepository.findAll().iterator().next();
        assertEquals("shinjw@naver.com", user.getEmail());
        assertNotNull(user.getPassword());
        assertNotEquals("1234", user.getPassword());
        assertEquals("신제우", user.getName());
    }

    @Test
    @DisplayName("회원가입시 중복된 이메일 체크")
    void test2() throws Exception {
        // given
        User user = User.builder()
                .email("shinjw@naver.com")
                .password("1234")
                .name("신제우2")
                .build();
        userRepository.save(user);

        Signup signup = Signup.builder()
                .name("신제우")
                .password("1234")
                .email("shinjw@naver.com")
                .build();

        // expected
        assertThrows(AlreadyExistsEmailException.class, () -> authService.signup(signup));
    }
}