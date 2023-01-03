package com.soonmin.final_project.domain.entity;

import com.soonmin.final_project.domain.UserRole;
import com.soonmin.final_project.domain.dto.user.UserDto;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class User extends UserBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String userName;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    public UserDto toDto(){
        return new UserDto(this.id, this.userName,this.password);
    }
}
