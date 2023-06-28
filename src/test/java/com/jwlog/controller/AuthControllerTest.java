package com.jwlog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwlog.domain.User;
import com.jwlog.repository.UserRepository;
import com.jwlog.request.Login;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
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
    private UserRepository userRepository;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void beforeEach() {
        userRepository.deleteAll();
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
    @DisplayName("로그인 요청 실패 - ID 또는 비밀번호 오입력")
    void test() throws Exception {
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

    private void insertUserEntity() {
        User user = User.builder()
                .name("신제우")
                .email("shinjw")
                .password("1234")
                .build();
        userRepository.save(user);
    }

}