package com.soonmin.final_project.domain.entity;

import com.soonmin.final_project.domain.dto.comment.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Builder
@SQLDelete(sql = "UPDATE comment SET deleted_at = current_timestamp WHERE id = ?")
@Where(clause = "deleted_at is null")
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

    private LocalDateTime deletedAt;

    public CommentDto toDto() {
        return CommentDto.builder()
                .id(this.id)
                .comment(this.comment)
                .post(this.post)
                .user(this.user)
                .createdAt(this.getCreatedAt())
                .build();
    }

    public void update(String comment) {
        this.comment = comment;
    }

}
