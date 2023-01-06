package com.soonmin.final_project.controller;

import com.soonmin.final_project.domain.dto.alarm.AlarmContainer;
import com.soonmin.final_project.domain.dto.alarm.AlarmResponse;
import com.soonmin.final_project.domain.Response;
import com.soonmin.final_project.service.AlarmService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequestMapping("/api/v1/alarms")
@RequiredArgsConstructor
public class AlarmRestController {

    private final AlarmService alarmService;

    @GetMapping
    @ApiOperation(value = "알람 조회", notes = "자신에게 온 알람 목록을 20개 단위로 조회합니다. 알람은 자신의 게시글에 댓글과 좋아요가 달릴 경우 생성됩니다. 로그인한 사용자만 조회 가능합니다.")
    public Response<AlarmContainer> getAlarms(@ApiIgnore Authentication authentication, @ApiIgnore @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        List<AlarmResponse> alarms = alarmService.getAlarms(authentication.getName(), pageable);
        return Response.success(new AlarmContainer(alarms));
    }
}
