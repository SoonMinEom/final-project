package com.soonmin.final_project.domain.dto;

import com.soonmin.final_project.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserJoinRequest {
    private String userName;
    private String password;

    public User toEntity() {
        return User.builder()
                .userName(this.userName)
                .password(this.password)
                .build();
    }
}
