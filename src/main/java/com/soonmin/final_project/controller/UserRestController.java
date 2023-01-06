package com.soonmin.final_project.controller;

import com.soonmin.final_project.domain.Response;
import com.soonmin.final_project.domain.dto.*;
import com.soonmin.final_project.domain.dto.user.*;
import com.soonmin.final_project.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserRestController {
    private final UserService userService;

    // 회원 가입
    @PostMapping("/join")
    @ApiOperation(value = "회원 가입", notes = "회원 가입을 합니다.")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request) {
        UserDto dto = userService.join(request);
        return Response.success(dto.toResponse());
    }

    // 로그인
    @PostMapping("/login")
    @ApiOperation(value = "로그인", notes = "로그인을 합니다. JWT 토큰을 발행합니다.")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        String token = userService.login(request);
        return Response.success(new UserLoginResponse(token));
    }

    // ADMIN 승급기능, 정말로 승급만 가능
    // 초기 어드민 : userName = admin, password = string
    @PostMapping("/{id}/role/change")
    @ApiOperation(value = "관리자 권한 부여", notes = "특정 유저에게 관리자 권한을 부여합니다. 이미 관리자 권한을 지닌 사용자만 권한 부여를 할 수 있습니다.")
    public Response<RoleChangeResponse> roleChange(@PathVariable Integer id, @ApiIgnore Authentication authentication) {
        UserDto userDto = userService.roleChange(id, authentication.getName());
        return Response.success(new RoleChangeResponse(userDto.getUserName(), "ADMIN 승급 성공"));
    }
}
