package com.jwlog.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Signup {

    private String email;
    private String password;
    private String name;
}
