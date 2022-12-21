package com.soonmin.final_project.domain.dto;

import com.soonmin.final_project.domain.UserRole;
import com.soonmin.final_project.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserJoinRequest {
    private String userName;
    private String password;

    public User toEntity(String password) {
        return User.builder()
                .userName(this.userName)
                .password(password)
                .role(UserRole.USER)
                .build();
    }
}
