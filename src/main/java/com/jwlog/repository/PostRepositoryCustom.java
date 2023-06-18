package com.jwlog.repository;

import com.jwlog.domain.Post;
import com.jwlog.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
