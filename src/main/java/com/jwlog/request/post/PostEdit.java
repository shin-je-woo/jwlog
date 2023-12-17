package com.jwlog.request.post;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostEdit {

    @NotBlank(message = "타이틀을 입력해주세요.")
    private final String title;

    @NotBlank(message = "콘텐츠를 입력해주세요.")
    private final String content;

    @Builder
    public PostEdit(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
