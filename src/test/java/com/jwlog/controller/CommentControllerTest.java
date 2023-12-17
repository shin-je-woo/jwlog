package com.jwlog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwlog.domain.Comment;
import com.jwlog.domain.Post;
import com.jwlog.domain.User;
import com.jwlog.repository.UserRepository;
import com.jwlog.repository.comment.CommentRepository;
import com.jwlog.repository.post.PostRepository;
import com.jwlog.request.comment.CommentCreate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class CommentControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @AfterEach
    void celan() {
        postRepository.deleteAll();
        userRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @Test
    @DisplayName("댓글 작성")
    void test() throws Exception {
        // given
        User user = User.builder()
                .name("신제우")
                .email("shinjw0926@naver.com")
                .password("1234")
                .build();
        userRepository.save(user);

        Post post = Post.builder()
                .title("글 제목")
                .content("글 내용")
                .user(user)
                .build();
        postRepository.save(post);

        CommentCreate request = CommentCreate.builder()
                .author("작성자")
                .password("비밀번호1234")
                .content("댓글입니다.댓글입니다.")
                .build();
        String json = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(post("/posts/{postId}/comments", post.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk());

        // then
        assertEquals(1L, commentRepository.count());

        Comment savedComment = commentRepository.findAll().get(0);
        assertEquals("작성자", savedComment.getAuthor());
        assertNotEquals("비밀번호1234", savedComment.getPassword());
        assertTrue(passwordEncoder.matches("비밀번호1234", savedComment.getPassword()));
        assertEquals("댓글입니다.댓글입니다.", savedComment.getContent());
    }
}