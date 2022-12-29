package com.soonmin.final_project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soonmin.final_project.domain.dto.PostCreateRequest;
import com.soonmin.final_project.domain.dto.PostDto;
import com.soonmin.final_project.domain.dto.PostUpdateRequest;
import com.soonmin.final_project.domain.dto.PostViewResponse;
import com.soonmin.final_project.exception.ErrorCode;
import com.soonmin.final_project.exception.LikeLionException;
import com.soonmin.final_project.forTest.WithUser;
import com.soonmin.final_project.service.AlgorithmService;
import com.soonmin.final_project.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HelloController.class)
@MockBean(JpaMetamodelMappingContext.class)
class HelloControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AlgorithmService algorithmService;

    @Test
    @WithMockUser
    @DisplayName("hello")
    void hello() throws Exception {

        mockMvc.perform(get("/api/v1/hello")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("엄순민"));
    }

    @Test
    @WithMockUser
    @DisplayName("자릿수의 합")
    void sumOfDigit() throws Exception {
        when(algorithmService.sumOfDigit(123)).thenReturn(6);

        mockMvc.perform(get("/api/v1/hello/123").with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("6"));

    }
}