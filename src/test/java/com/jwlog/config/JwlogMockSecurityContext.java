package com.jwlog.config;

import com.jwlog.domain.User;
import com.jwlog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

@RequiredArgsConstructor
public class JwlogMockSecurityContext implements WithSecurityContextFactory<JwlogMockUser> {

    private final UserRepository userRepository;

    @Override
    public SecurityContext createSecurityContext(JwlogMockUser annotation) {
        User user = User.builder()
                .name(annotation.name())
                .email(annotation.email())
                .password(annotation.password())
                .build();
        userRepository.save(user);

        UserPrincipal userPrincipal = new UserPrincipal(user);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userPrincipal,
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(token);

        return securityContext;
    }
}
