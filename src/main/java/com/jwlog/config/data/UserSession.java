package com.jwlog.config.data;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserSession {

    private String name;

    public UserSession(String name) {
        this.name = name;
    }
}
