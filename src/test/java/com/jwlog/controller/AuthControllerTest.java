package com.jwlog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwlog.domain.Session;
import com.jwlog.domain.User;
import com.jwlog.repository.SessionRepository;
import com.jwlog.repository.UserRepository;
import com.jwlog.request.Login;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    SessionRepository sessionRepository;

    @BeforeEach
    void beforeEach() {
        userRepository.deleteAll();
        sessionRepository.deleteAll();
    }

    @Test
    @DisplayName("로그인 요청 성공")
    void test1() throws Exception {
        // given
        insertUserEntity();
        Login login = Login.builder()
                .email("shinjw")
                .password("1234")
                .build();
        String loginJson = objectMapper.writeValueAsString(login);

        // expected
        mockMvc.perform(post("/auth/login")
                .contentType(APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @DisplayName("로그인 요청 성공 - 세션 1개 생성")
    void test2() throws Exception {
        // given
        User user = insertUserEntity();
        Login login = Login.builder()
                .email("shinjw")
                .password("1234")
                .build();
        String loginJson = objectMapper.writeValueAsString(login);

        // expected
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andReturn();

        Session session = sessionRepository.findByUser(user)
                .orElseThrow(RuntimeException::new);

        assertEquals(session.getUser().getId(), user.getId());
    }

    @Test
    @DisplayName("로그인 요청 성공 후 세션 응답")
    void test3() throws Exception {
        // given
        User user = insertUserEntity();
        Login login = Login.builder()
                .email("shinjw")
                .password("1234")
                .build();
        String loginJson = objectMapper.writeValueAsString(login);

        // expected
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", notNullValue()))
                .andDo(print());

        Session session = sessionRepository.findByUser(user)
                .orElseThrow(RuntimeException::new);

        assertEquals(session.getUser().getId(), user.getId());
    }

    @Test
    @DisplayName("로그인 요청 실패 - ID 또는 비밀번호 오입력")
    void test4() throws Exception {
        // given
        insertUserEntity();
        Login login = Login.builder()
                .email("shinjw")
                .password("343434")
                .build();
        String loginJson = objectMapper.writeValueAsString(login);

        // when
        mockMvc.perform(post("/auth/login")
                .contentType(APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("아이디 또는 비밀번호가 올바르지 않습니다."))
                .andExpect(jsonPath("$.validation").value(""))
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 후 권한이 필요한 페이지에 접속")
    void test5() throws Exception {
        // given
        User user = insertUserEntity();
        Session session = user.addSession();
        userRepository.save(user);

        // expected
        mockMvc.perform(get("/test")
                        .header("Authorization", session.getAccessToken())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 후 검증되지 않은 토큰값으로 권한이 필요한 페이지에 접속할 수 없다.")
    void test6() throws Exception {
        // given
        User user = insertUserEntity();
        Session session = user.addSession();
        userRepository.save(user);

        // expected
        mockMvc.perform(get("/test")
                        .header("Authorization", session.getAccessToken() + "12345")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    private User insertUserEntity() {
        User user = User.builder()
                .name("신제우")
                .email("shinjw")
                .password("1234")
                .build();
        return userRepository.save(user);
    }

}