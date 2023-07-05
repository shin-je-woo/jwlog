package com.jwlog.service;

import com.jwlog.crypto.PasswordEncoder;
import com.jwlog.domain.User;
import com.jwlog.exception.AlreadyExistsEmailException;
import com.jwlog.repository.UserRepository;
import com.jwlog.request.Signup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public void signup(Signup signup) {
        valdateDuplEmail(signup);

        String encryptedPassword = PasswordEncoder.encrypt(signup.getPassword());
        User user = User.builder()
                .name(signup.getName())
                .password(encryptedPassword)
                .email(signup.getEmail())
                .build();

        userRepository.save(user);
    }

    private void valdateDuplEmail(Signup signup) {
        Optional<User> userOptional = userRepository.findByEmail(signup.getEmail());
        if(userOptional.isPresent()) throw new AlreadyExistsEmailException();
    }
}
