package com.jwlog.repository;

import com.jwlog.domain.Session;
import com.jwlog.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {

    Optional<Session> findByUser(User user);

    Optional<Session> findByAccessToken(String accessToken);
}
