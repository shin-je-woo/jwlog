package com.jwlog.request;

import lombok.*;

import static java.lang.Math.max;
import static java.lang.Math.min;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostSearch {

    private static final int MAX_SIZE = 1000;

    @Builder.Default
    private Integer page = 1;

    @Builder.Default
    private Integer size = 20;

    public long getOffset() {
        return (long) (max(1, (page)) - 1) * (min(size, MAX_SIZE));
    }
}
