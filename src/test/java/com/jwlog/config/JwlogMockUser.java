package com.jwlog.config;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = JwlogMockSecurityContext.class)
public @interface JwlogMockUser {

    String name() default "신제우";

    String email() default "shinjw0926@naver.com";

    String password() default "";
}
