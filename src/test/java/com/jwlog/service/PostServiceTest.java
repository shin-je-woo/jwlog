package com.jwlog.service;

import com.jwlog.domain.Post;
import com.jwlog.domain.User;
import com.jwlog.exception.PostNotFound;
import com.jwlog.repository.post.PostRepository;
import com.jwlog.repository.UserRepository;
import com.jwlog.request.post.PostCreate;
import com.jwlog.request.post.PostEdit;
import com.jwlog.request.post.PostSearch;
import com.jwlog.response.PostResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    PostService postService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @BeforeEach
    void beforeEach() {
        userRepository.deleteAll();
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void test1() throws Exception {
        // given
        User user = User.builder()
                .name("신제우")
                .email("shinjw0926@naver.com")
                .password("1234")
                .build();
        userRepository.save(user);

        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다")
                .content("내용입니다")
                .build();

        // when
        postService.write(user.getId(), postCreate);

        // then
        assertEquals(1, postRepository.count());
        Post post = postRepository.findAll().get(0);
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

    @Test
    @DisplayName("글 제목만 수정, 글 내용은 변경 안됨")
    void test4() throws Exception {
        // given
        Post post = Post.builder()
                .title("글 제목")
                .content("글 내용")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("변경 제목")
                .build();

        // when
        postService.edit(post.getId(), postEdit);

        // then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다. id = " + post.getId()));

        assertEquals(postEdit.getTitle(), changedPost.getTitle());
        assertEquals(post.getContent(), changedPost.getContent());
    }

    @Test
    @DisplayName("글 제목, 글 내용 수정")
    void test5() throws Exception {
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

        // when
        postService.edit(post.getId(), postEdit);

        // then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다. id = " + post.getId()));

        assertEquals(postEdit.getTitle(), changedPost.getTitle());
        assertEquals(postEdit.getContent(), changedPost.getContent());
    }

    @Test
    @DisplayName("게시글 삭제")
    void test6() throws Exception {
        // given
        Post post = Post.builder()
                .title("글 제목")
                .content("글 내용")
                .build();
        postRepository.save(post);

        // when
        postService.delete(post.getId());

        // then
        assertEquals(0, postRepository.count());
    }

    @Test
    @DisplayName("글 1개 조회 - 글이 없는 경우 PostNotFound 예외를 던진다.")
    void test7() throws Exception {
        // given
        Post post = Post.builder()
                .title("글 제목")
                .content("글 내용")
                .build();
        postRepository.save(post);

        // expected
        assertThrows(PostNotFound.class, () -> postService.get(post.getId() + 1L));
    }

    @Test
    @DisplayName("게시글 삭제 - 글이 없는 경우 PostNotFound 예외를 던진다.")
    void test8() throws Exception {
        // given
        Post post = Post.builder()
                .title("글 제목")
                .content("글 내용")
                .build();
        postRepository.save(post);

        // expected
        assertThrows(PostNotFound.class, () -> postService.delete(post.getId() + 1L));
    }

    @Test
    @DisplayName("글 수정 - 글이 없는 경우 PostNotFound 예외를 던진다.")
    void test9() throws Exception {
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
        assertThrows(PostNotFound.class, () -> postService.edit(post.getId() + 1L, postEdit));
    }
}