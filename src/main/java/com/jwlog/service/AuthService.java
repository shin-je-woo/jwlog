package com.jwlog.service;

import com.jwlog.crypto.PasswordEncoder;
import com.jwlog.domain.User;
import com.jwlog.exception.AlreadyExistsEmailException;
import com.jwlog.exception.InvalidSigninInfo;
import com.jwlog.repository.UserRepository;
import com.jwlog.request.Login;
import com.jwlog.request.Signup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    @Transactional
    public Long signin(Login login) {

        User user = userRepository.findByEmail(login.getEmail())
                .orElseThrow(InvalidSigninInfo::new);

        if (!PasswordEncoder.matches(login.getPassword(), user.getPassword())) {
            throw new InvalidSigninInfo();
        }

        return user.getId();
    }

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
