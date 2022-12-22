package com.soonmin.final_project.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostCreateResponse {
    private String message;
    private Integer postId;
}
