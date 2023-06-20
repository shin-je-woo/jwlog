package com.jwlog.exception;

import java.util.HashMap;
import java.util.Map;

public abstract class JwlogException extends RuntimeException {

    private final Map<String, String> validation = new HashMap<>();

    public JwlogException(String message) {
        super(message);
    }

    public JwlogException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();

    public void addValidation(String fieldName, String errorMessage) {
        this.validation.put(fieldName, errorMessage);
    }

    public Map<String, String> getValidation() {
        return new HashMap<>(validation);
    }
}
