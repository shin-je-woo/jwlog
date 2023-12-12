package com.jwlog.controller;

import com.jwlog.config.UserPrincipal;
import com.jwlog.request.Signup;
import com.jwlog.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/auth/login")
    public String login() {
        return "로그인 페이지입니다.";
    }

    @GetMapping("/auth/user")
    public String user(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return "유저 페이지입니다.";
    }

    @GetMapping("/auth/admin")
    public String admin() {
        return "어드민 페이지입니다.";
    }

    @PostMapping("/auth/signup")
    public void signup(@RequestBody Signup signup) {
        authService.signup(signup);
    }
}
