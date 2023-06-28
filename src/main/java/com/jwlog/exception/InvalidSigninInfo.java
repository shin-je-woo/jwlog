package com.jwlog.exception;

import org.springframework.http.HttpStatus;

public class InvalidSigninInfo extends JwlogException {

    private static final String MESSAGE = "아이디 또는 비밀번호가 올바르지 않습니다.";

    public InvalidSigninInfo() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }
}
