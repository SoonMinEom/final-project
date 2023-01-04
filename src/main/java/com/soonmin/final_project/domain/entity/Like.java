package com.soonmin.final_project.domain.entity;

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
@SQLDelete(sql = "UPDATE likes SET deleted_at = current_timestamp WHERE id = ?")
@Where(clause = "deleted_at is null")
@Table(name = "likes")
public class Like extends PostBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime deletedAt;

    public static Like pushLike(Post post, User user) {
        return Like.builder()
                .post(post)
                .user(user)
                .build();
    }



}
