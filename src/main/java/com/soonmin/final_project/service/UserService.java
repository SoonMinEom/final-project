package com.soonmin.final_project.service;

import com.soonmin.final_project.domain.dto.UserDto;
import com.soonmin.final_project.domain.dto.UserJoinRequest;
import com.soonmin.final_project.domain.dto.UserLoginRequest;
import com.soonmin.final_project.domain.entity.User;
import com.soonmin.final_project.exception.ErrorCode;
import com.soonmin.final_project.exception.LikeLionException;
import com.soonmin.final_project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDto join(UserJoinRequest request) {
        userRepository.findByUserName(request.getUserName())
                .ifPresent(user -> {throw new LikeLionException(ErrorCode.DUPLICATED_USER_NAME, ErrorCode.DUPLICATED_USER_NAME.getMessage());});
        User savedUser = userRepository.save(request.toEntity());
        return savedUser.toDto();
    }

    public String login(UserLoginRequest request) {
        return "token";
    }
}
