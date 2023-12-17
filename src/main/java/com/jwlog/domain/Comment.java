package com.jwlog.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(
        indexes = {
                @Index(name = "IDX_COMMENT_POST_ID", columnList = "post_id")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @Setter
    private Post post;

    @Builder
    public Comment(Long id, String author, String password, String content) {
        this.id = id;
        this.author = author;
        this.password = password;
        this.content = content;
    }
}
