package com.jwlog.service;

import com.jwlog.domain.User;
import com.jwlog.exception.InvalidSigninInfo;
import com.jwlog.repository.UserRepository;
import com.jwlog.request.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public void signin(Login login) {
        User user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
                .orElseThrow(InvalidSigninInfo::new);
        user.addSession();
    }
}
