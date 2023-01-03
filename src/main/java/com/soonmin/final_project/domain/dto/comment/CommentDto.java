package com.soonmin.final_project.domain.dto.comment;

import com.soonmin.final_project.domain.entity.Post;
import com.soonmin.final_project.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CommentDto {

    private Integer id;
    private String comment;
    private Post post;
    private User user;
    private LocalDateTime createdAt;

    public CommentResponse toResponse() {
        return CommentResponse.builder()
                .id(this.id)
                .comment(this.comment)
                .userName(this.user.getUserName())
                .postId(this.post.getId())
                .createdAt(this.createdAt)
                .build();
    }
}
