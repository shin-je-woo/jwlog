package com.jwlog.service;

import com.jwlog.domain.Session;
import com.jwlog.domain.User;
import com.jwlog.exception.InvalidSigninInfo;
import com.jwlog.repository.UserRepository;
import com.jwlog.request.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    @Transactional
    public Long signin(Login login) {
        User user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
                .orElseThrow(InvalidSigninInfo::new);
        Session session = user.addSession();

        return user.getId();
    }
}
