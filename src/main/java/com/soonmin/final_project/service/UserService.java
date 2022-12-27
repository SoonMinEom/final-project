package com.soonmin.final_project.service;

import com.soonmin.final_project.domain.UserRole;
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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    @Value("${jwt.token.secret}")
    private String secretKey;
    private long expireTime = 1000*60*60; // 1시간

    // 회원 가입
    public UserDto join(UserJoinRequest request) {
        userRepository.findByUserName(request.getUserName())
                .ifPresent(user -> {throw new LikeLionException(ErrorCode.DUPLICATED_USER_NAME, ErrorCode.DUPLICATED_USER_NAME.getMessage());});
        User savedUser = userRepository.save(request.toEntity(encoder.encode(request.getPassword())));
        return savedUser.toDto();
    }

    // 로그인
    public String login(UserLoginRequest request) {
        User user = userRepository.findByUserName(request.getUserName())
                .orElseThrow(() -> new LikeLionException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));

        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new LikeLionException(ErrorCode.INVALID_PASSWORD, ErrorCode.INVALID_PASSWORD.getMessage());
        }

        return JwtUtil.createToken(request.getUserName(),secretKey,expireTime);
    }

    // 어드민 승급
    @Transactional
    public UserDto roleChange(Integer id, String userName) {
        User admin = userRepository.findByUserName(userName).orElseThrow(()->new LikeLionException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));

        if(admin.getRole() != UserRole.ADMIN) {
            throw new LikeLionException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
        }

        User user = userRepository.findById(id).orElseThrow(()->new LikeLionException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));
        user.setRole(UserRole.ADMIN);

        return userRepository.save(user).toDto();
    }

    public User getUserByUserName(String userName) {
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new LikeLionException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));
    }
}
