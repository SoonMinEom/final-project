package com.soonmin.final_project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soonmin.final_project.domain.UserRole;
import com.soonmin.final_project.domain.dto.comment.CommentCreateRequest;
import com.soonmin.final_project.domain.dto.comment.CommentDto;
import com.soonmin.final_project.domain.entity.Post;
import com.soonmin.final_project.domain.entity.User;
import com.soonmin.final_project.exception.ErrorCode;
import com.soonmin.final_project.exception.LikeLionException;
import com.soonmin.final_project.service.CommentService;
import com.soonmin.final_project.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(PostController.class)
@MockBean(JpaMetamodelMappingContext.class)
class CommentControllerTest {

    @MockBean
    CommentService commentService;

    @MockBean
    PostService postService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    User user;
    Post post;
    CommentDto dto;

    CommentCreateRequest commentCreateRequest;

    @BeforeEach
    void beforeEach() {
        user = new User(1, "name", "password", UserRole.USER);
        post = new Post(1, "title","body",user);
        commentCreateRequest = new CommentCreateRequest("comment test");
        dto = CommentDto.builder()
                .id(1)
                .comment("test")
                .post(post)
                .user(user)
                .build();
    }

    @Test
    @WithMockUser
    @DisplayName("댓글 작성 성공")
    void create_success() throws Exception {

        when(commentService.create(any(), any(), any())).thenReturn(dto);

        mockMvc.perform(post("/api/v1/posts/1/comments")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(commentCreateRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"));
    }

    @Test
    @WithAnonymousUser
    @DisplayName("댓글 작성 실패(1) - 로그인 하지 않은 경우")
    void create_fail1() throws Exception {
        when(commentService.create(any(), any(), any())).thenThrow(new LikeLionException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));

        mockMvc.perform(post("/api/v1/posts/1/comments")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(commentCreateRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    @DisplayName("댓글 작성 실패(2) - 게시물이 존재하지 않는 경우")
    void create_fail2() throws Exception {
        when(commentService.create(any(), any(), any())).thenThrow(new LikeLionException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        mockMvc.perform(post("/api/v1/posts/1/comments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentCreateRequest)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.result.errorCode").value("POST_NOT_FOUND"));
    }
}