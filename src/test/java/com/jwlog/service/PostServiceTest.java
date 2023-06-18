package com.jwlog.service;

import com.jwlog.domain.Post;
import com.jwlog.repository.PostRepository;
import com.jwlog.request.PostCreate;
import com.jwlog.request.PostSearch;
import com.jwlog.response.PostResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class PostServiceTest {

    @Autowired
    PostService postService;

    @Autowired
    PostRepository postRepository;

    @BeforeEach
    void beforeEach() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void test1() throws Exception {
        // given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다")
                .content("내용입니다")
                .build();

        // when
        postService.write(postCreate);
        Post post = postRepository.findAll().get(0);

        // then
        assertEquals(1, postRepository.count());
        assertEquals("제목입니다", post.getTitle());
        assertEquals("내용입니다", post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void test2() throws Exception {
        // given
        Post post = Post.builder()
                .title("글 제목")
                .content("글 내용")
                .build();
        postRepository.save(post);
        Long postId = post.getId();

        // when
        PostResponse findPost = postService.get(postId);

        // then
        assertNotNull(findPost);
        assertEquals(1, postRepository.count());
        assertEquals("글 제목", findPost.getTitle());
        assertEquals("글 내용", findPost.getContent());
    }

    @Test
    @DisplayName("글 1페이지 조회")
    void test3() throws Exception {
        // given
        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                        .title("글 제목 " + i)
                        .content("글 내용 " + i)
                        .build())
                .toList();
        postRepository.saveAll(requestPosts);

        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .build();

        // when
        List<PostResponse> posts = postService.getList(postSearch);

        // then
        assertEquals(20, posts.size());
        assertEquals("글 제목 30", posts.get(0).getTitle());
        assertEquals("글 제목 26", posts.get(4).getTitle());
    }
}