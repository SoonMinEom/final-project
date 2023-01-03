package com.soonmin.final_project.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserDto {

    private Integer id;
    private String userName;
    private String password;

    public UserJoinResponse toResponse() {
        return new UserJoinResponse(this.id, this.userName);
    }
}
