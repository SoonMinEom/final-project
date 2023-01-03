package com.soonmin.final_project.domain.entity;

import com.soonmin.final_project.domain.dto.comment.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Builder
public class Comment extends PostBase{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String comment;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public CommentDto toDto() {
        return CommentDto.builder()
                .id(this.id)
                .comment(this.comment)
                .post(this.post)
                .user(this.user)
                .createdAt(this.getCreatedAt())
                .build();
    }

}
