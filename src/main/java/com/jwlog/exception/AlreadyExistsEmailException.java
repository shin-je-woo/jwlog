package com.jwlog.exception;

import org.springframework.http.HttpStatus;

public class AlreadyExistsEmailException extends JwlogException {

    private static final String MESSAGE = "이미 가입된 이메일입니다.";

    public AlreadyExistsEmailException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }
}
