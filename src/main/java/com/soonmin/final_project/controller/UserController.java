package com.soonmin.final_project.controller;

import com.soonmin.final_project.domain.dto.UserDto;
import com.soonmin.final_project.domain.dto.UserJoinRequest;
import com.soonmin.final_project.domain.dto.UserJoinResponse;
import com.soonmin.final_project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<UserJoinResponse> join(@RequestBody UserJoinRequest request) {
        UserDto dto = userService.join(request);
        return ResponseEntity.ok().body(dto.toResponse());
    }
}
