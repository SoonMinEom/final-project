package com.soonmin.final_project.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostUpdateRequest {
    private String title;
    private String body;
}
