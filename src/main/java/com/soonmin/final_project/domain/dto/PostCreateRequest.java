package com.soonmin.final_project.domain.dto;

import com.soonmin.final_project.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostCreateRequest {
    private String title;
    private String body;

    public Post toEntity() {
        return Post.builder()
                .title(this.title)
                .body(this.body)
                .build();
    }
}
