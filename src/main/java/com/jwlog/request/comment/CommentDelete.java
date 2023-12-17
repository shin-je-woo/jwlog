package com.jwlog.request.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentDelete {

    @NotBlank(message = "패스워드를 입력해주세요.")
    private String password;

    public CommentDelete(String password) {
        this.password = password;
    }
}
