package com.jwlog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwlog.domain.Post;
import com.jwlog.repository.PostRepository;
import com.jwlog.request.PostCreate;
import com.jwlog.request.PostEdit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void beforeEach() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성 요청")
    void test() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .title("제목입니다")
                .content("내용입니다")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("posts 요청시 title값은 필수다.")
    void test2() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .content("내용입니다")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("글 작성 요청시 DB에 값이 저장된다.")
    void test3() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .title("제목입니다")
                .content("내용입니다")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());

        // then
        assertEquals(1, postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다", post.getTitle());
        assertEquals("내용입니다", post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void test4() throws Exception {
        // given
        Post post = Post.builder()
                .title("글 제목")
                .content("글 내용")
                .build();
        postRepository.save(post);

        // expected
        mockMvc.perform(get("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("글 제목"))
                .andExpect(jsonPath("$.content").value("글 내용"))
                .andDo(print());
    }

    @Test
    @DisplayName("글 1페이지 조회")
    void test5() throws Exception {
        // given
        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                        .title("글 제목 " + i)
                        .content("글 내용 " + i)
                        .build())
                .toList();
        postRepository.saveAll(requestPosts);

        // expected
        mockMvc.perform(get("/posts?page=1&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$[0].title").value("글 제목 30"))
                .andExpect(jsonPath("$[0].content").value("글 내용 30"))
                .andDo(print());

        mockMvc.perform(get("/posts?page=1&")
                        .contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(20)))
                .andExpect(jsonPath("$[0].title").value("글 제목 30"))
                .andExpect(jsonPath("$[0].content").value("글 내용 30"))
                .andDo(print());
    }

    @Test
    @DisplayName("페이지를 0으로 요청하면 첫 페이지를 불러온다.")
    void test6() throws Exception {
        // given
        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                        .title("글 제목 " + i)
                        .content("글 내용 " + i)
                        .build())
                .toList();
        postRepository.saveAll(requestPosts);

        // expected
        mockMvc.perform(get("/posts?page=0&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$[0].title").value("글 제목 30"))
                .andExpect(jsonPath("$[0].content").value("글 내용 30"))
                .andDo(print());
    }

    @Transactional
    @Test
    @DisplayName("글 제목, 내용 수정")
    void test7() throws Exception {
        // given
        Post post = Post.builder()
                .title("글 제목")
                .content("글 내용")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("변경 제목")
                .content("변경 내용")
                .build();

        // expected
        mockMvc.perform(patch("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isOk())
                .andDo(print());

        assertEquals(postEdit.getTitle(), post.getTitle());
        assertEquals(postEdit.getContent(), post.getContent());
    }

    @Test
    @DisplayName("게시글 삭제")
    void test8() throws Exception {
        // given
        Post post = Post.builder()
                .title("글 제목")
                .content("글 내용")
                .build();
        postRepository.save(post);

        // expected
        mockMvc.perform(delete("/posts/{postId}", post.getId())
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        boolean isPresent = postRepository.findById(post.getId()).isPresent();
        assertFalse(isPresent);
    }

    @Test
    @DisplayName("존재하지 않는 게시글 조회")
    void test9() throws Exception {
        // expected
        mockMvc.perform(get("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 수정")
    void test10() throws Exception {
        // given
        PostEdit postEdit = PostEdit.builder()
                .title("변경 제목")
                .content("변경 내용")
                .build();

        // expected
        mockMvc.perform(patch("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 작성 시 제목에 '제우'라는 단어는 포함될 수 없다.")
    void test11() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .title("제우가 작성한 글입니다")
                .content("글 내용")
                .build();
        String json = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}