package com.jwlog.service;

import com.jwlog.domain.Session;
import com.jwlog.domain.User;
import com.jwlog.exception.AlreadyExistsEmailException;
import com.jwlog.exception.InvalidSigninInfo;
import com.jwlog.repository.UserRepository;
import com.jwlog.request.Login;
import com.jwlog.request.Signup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
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
        User user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
                .orElseThrow(InvalidSigninInfo::new);
        Session session = user.addSession();

        return user.getId();
    }

    public void signup(Signup signup) {
        valdateDuplEmail(signup);

        SCryptPasswordEncoder encoder = new SCryptPasswordEncoder(16, 8, 1, 32, 64);
        String encryptedPassword = encoder.encode(signup.getPassword());

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
