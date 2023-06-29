package com.jwlog.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accessToken;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Session(User user) {
        this.accessToken = UUID.randomUUID().toString();
        this.user = user;
    }
}
