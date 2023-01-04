package com.soonmin.final_project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soonmin.final_project.domain.dto.alarm.AlarmResponse;
import com.soonmin.final_project.domain.dto.alarm.AlarmType;
import com.soonmin.final_project.exception.ErrorCode;
import com.soonmin.final_project.exception.LikeLionException;
import com.soonmin.final_project.service.AlarmService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AlarmController.class)
@MockBean(JpaMetamodelMappingContext.class)
class AlarmControllerTest {

    @MockBean
    AlarmService alarmService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    List<AlarmResponse> alarmList;
    @BeforeEach
    void beforeEach() {
        alarmList = new ArrayList<>();
        alarmList.add(new AlarmResponse(1, AlarmType.NEW_LIKE_ON_POST,1,1, AlarmType.NEW_LIKE_ON_POST.getAlarmText(), LocalDateTime.now()));
        alarmList.add(new AlarmResponse(2, AlarmType.NEW_COMMENT_ON_POST,2,2, AlarmType.NEW_COMMENT_ON_POST.getAlarmText(), LocalDateTime.now()));
    }

    @Test
    @WithMockUser
    @DisplayName("알람 목록 조회 성공")
    void getAlarms_success() throws Exception {
        when(alarmService.getAlarms(any(), any())).thenReturn(alarmList);

        mockMvc.perform(get("/api/v1/alarms").with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.content").exists());
    }

    @Test
    @WithAnonymousUser
    @DisplayName("알람 목록 조회 실패")
    void getAlarms_fail() throws Exception {
        when(alarmService.getAlarms(any(), any())).thenThrow(new LikeLionException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));

        mockMvc.perform(get("/api/v1/alarms").with(csrf()))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}