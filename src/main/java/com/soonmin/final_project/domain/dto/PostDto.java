package com.soonmin.final_project.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostDto {
    private Integer id;
    private String title;
    private String body;

    public PostCreateResponse toResponse() {
        return new PostCreateResponse("포스트 등록 완료", this.id);
    }
}
