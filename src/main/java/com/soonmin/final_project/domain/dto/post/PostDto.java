package com.soonmin.final_project.domain.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class PostDto {
    private Integer id;
    private String title;
    private String body;
    private String userName;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    public PostCreateResponse toCreateResponse() {
        return new PostCreateResponse("포스트 등록 완료", this.id);
    }
    public PostViewResponse toViewResponse() {
        return new PostViewResponse(this.id, this.title, this.body, this.userName, this.createdAt, this.lastModifiedAt);
    }
}
