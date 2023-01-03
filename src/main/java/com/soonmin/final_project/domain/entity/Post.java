package com.soonmin.final_project.domain.entity;

import com.soonmin.final_project.domain.dto.post.PostDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Post extends PostBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String body;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public PostDto toDto() {
        return new PostDto(this.id, this.title, this.body,this.user.getUserName(), this.getCreatedAt(), this.getLastModifiedAt());
    }

    public void update(String title, String body) {
        this.title = title;
        this.body = body;
    }
}
