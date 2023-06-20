package com.jwlog.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InvaldRequest extends JwlogException {

    private static final String MESSAGE = "잘못된 요청입니다.";
    private String filedName;
    private String errorMessage;

    public InvaldRequest() {
        super(MESSAGE);
    }

    public InvaldRequest(String filedName, String errorMessage) {
        super(MESSAGE);
        this.filedName = filedName;
        this.errorMessage = errorMessage;
        super.addValidation(filedName, errorMessage);
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }
}
