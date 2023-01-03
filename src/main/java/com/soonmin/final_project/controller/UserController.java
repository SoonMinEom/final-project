package com.soonmin.final_project.controller;

import com.soonmin.final_project.domain.Response;
import com.soonmin.final_project.domain.dto.*;
import com.soonmin.final_project.domain.dto.user.*;
import com.soonmin.final_project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // 회원 가입
    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request) {
        UserDto dto = userService.join(request);
        return Response.success(dto.toResponse());
    }

    // 로그인
    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        String token = userService.login(request);
        return Response.success(new UserLoginResponse(token));
    }

    // ADMIN 승급기능, 정말로 승급만 가능
    // 초기 어드민 : userName = admin, password = string
    @PostMapping("/{id}/role/change")
    public Response<RoleChangeResponse> roleChange(@PathVariable Integer id, Authentication authentication) {
        UserDto userDto = userService.roleChange(id, authentication.getName());
        return Response.success(new RoleChangeResponse(userDto.getUserName(), "ADMIN 승급 성공"));
    }
}
