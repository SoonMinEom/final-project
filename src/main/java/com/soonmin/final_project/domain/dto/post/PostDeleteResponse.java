package com.soonmin.final_project.domain.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostDeleteResponse {
    private String message;
    private Integer postId;
}
