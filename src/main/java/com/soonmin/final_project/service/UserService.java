package com.soonmin.final_project.service;

import com.soonmin.final_project.domain.dto.UserDto;
import com.soonmin.final_project.domain.dto.UserJoinRequest;
import com.soonmin.final_project.domain.dto.UserLoginRequest;
import com.soonmin.final_project.domain.entity.User;
import com.soonmin.final_project.exception.ErrorCode;
import com.soonmin.final_project.exception.LikeLionException;
import com.soonmin.final_project.repository.UserRepository;
import com.soonmin.final_project.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    @Value("${jwt.token.secret}")
    private String secretKey;
    private long expireTime = 1000 * 60 * 60; // 1시간

    public UserDto join(UserJoinRequest request) {
        userRepository.findByUserName(request.getUserName())
                .ifPresent(user -> {throw new LikeLionException(ErrorCode.DUPLICATED_USER_NAME, ErrorCode.DUPLICATED_USER_NAME.getMessage());});
        User savedUser = userRepository.save(request.toEntity(encoder.encode(request.getPassword())));
        return savedUser.toDto();
    }

    public String login(UserLoginRequest request) {
        return JwtUtil.createToken(request.getUserName(),secretKey,expireTime);
    }
}
