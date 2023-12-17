package com.jwlog.repository.post;

import com.jwlog.domain.Post;
import com.jwlog.request.post.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
