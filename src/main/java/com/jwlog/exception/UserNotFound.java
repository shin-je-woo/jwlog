package com.jwlog.exception;

import org.springframework.http.HttpStatus;

public class UserNotFound extends JwlogException {

    private static final String MESSAGE = "존재하지 않는 사용자입니다.";

    public UserNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.NOT_FOUND.value();
    }
}
