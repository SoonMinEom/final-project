package com.soonmin.final_project.controller;

import com.soonmin.final_project.domain.dto.alarm.AlarmContainer;
import com.soonmin.final_project.domain.dto.alarm.AlarmResponse;
import com.soonmin.final_project.domain.Response;
import com.soonmin.final_project.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/alarms")
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;

    @GetMapping
    public Response<AlarmContainer> getAlarms(Authentication authentication, @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        List<AlarmResponse> alarms = alarmService.getAlarms(authentication.getName(), pageable);
        return Response.success(new AlarmContainer(alarms));
    }
}
