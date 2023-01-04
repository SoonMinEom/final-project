package com.soonmin.final_project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soonmin.final_project.domain.UserRole;
import com.soonmin.final_project.domain.dto.comment.CommentRequest;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
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
    CommentRequest commentRequest;

    @BeforeEach
    void beforeEach() {
        user = new User(1, "name", "password", UserRole.USER);
        post = new Post(1, "title","body", user, LocalDateTime.now());
        commentRequest = new CommentRequest("comment test");
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

        when(commentService.create(any(), any(), any())).thenReturn(mock(CommentDto.class));

        mockMvc.perform(post("/api/v1/posts/1/comments")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(commentRequest)))
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
                    .content(objectMapper.writeValueAsBytes(commentRequest)))
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
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.result.errorCode").value("POST_NOT_FOUND"));
    }

    @Test
    @WithMockUser
    @DisplayName("댓글 목록 조회 성공")
    void view_success() throws Exception {

        mockMvc.perform(get("/api/v1/posts/1/comments")
                        .with(csrf())
                        .param("sort", "createdAt,desc"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("댓글 수정 성공")
    void update_success() throws Exception {
        when(commentService.update(any(), any(), any(), any())).thenReturn(dto);

        mockMvc.perform(put("/api/v1/posts/1/comments/1")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.comment").value("test"));
    }

    @Test
    @WithAnonymousUser
    @DisplayName("댓글 수정 실패(1) : 인증 실패")
    void update_fail1() throws Exception {
        when(commentService.update(any(), any(), any(), any())).thenThrow(new LikeLionException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(put("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    @DisplayName("댓글 수정 실패(2) : 댓글 불일치")
    void update_fail2() throws Exception {
        when(commentService.update(any(), any(), any(), any())).thenThrow(new LikeLionException(ErrorCode.COMMENT_NOT_FOUND, ErrorCode.COMMENT_NOT_FOUND.getMessage()));

        mockMvc.perform(put("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DisplayName("댓글 수정 실패(3) : 작성자 불일치")
    void update_fail3() throws Exception {
        when(commentService.update(any(), any(), any(), any())).thenThrow(new LikeLionException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(put("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    @DisplayName("댓글 수정 실패(4) : 데이터베이스 에러")
    void update_fail4() throws Exception {
        when(commentService.update(any(), any(), any(), any())).thenThrow(new LikeLionException(ErrorCode.DATABASE_ERROR, ErrorCode.DATABASE_ERROR.getMessage()));

        mockMvc.perform(put("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser
    @DisplayName("댓글 수정 실패(5) : Post 없는 경우")
    void update_fail5() throws Exception {
        when(commentService.update(any(), any(), any(), any())).thenThrow(new LikeLionException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        mockMvc.perform(put("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DisplayName("댓글 삭제 성공")
    void delete_success() throws Exception {
        when(commentService.delete(any(), any(), any())).thenReturn(1);

        mockMvc.perform(delete("/api/v1/posts/1/comments/1")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.id").value(1));
    }

    @Test
    @WithAnonymousUser
    @DisplayName("댓글 삭제 실패(1) : 인증 실패")
    void delete_fail1() throws Exception {
        when(commentService.delete(any(), any(), any())).thenThrow(new LikeLionException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(delete("/api/v1/posts/1/comments/1")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    @DisplayName("댓글 삭제 실패(2) : 댓글 불일치")
    void delete_fail2() throws Exception {
        when(commentService.delete(any(), any(), any())).thenThrow(new LikeLionException(ErrorCode.COMMENT_NOT_FOUND, ErrorCode.COMMENT_NOT_FOUND.getMessage()));

        mockMvc.perform(delete("/api/v1/posts/1/comments/1")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DisplayName("댓글 삭제 실패(3) : 작성자 불일치")
    void delete_fail3() throws Exception {
        when(commentService.delete(any(), any(), any())).thenThrow(new LikeLionException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(delete("/api/v1/posts/1/comments/1")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    @DisplayName("댓글 삭제 실패(4) : 데이터베이스 에러")
    void delete_fail4() throws Exception {
        when(commentService.delete(any(), any(), any())).thenThrow(new LikeLionException(ErrorCode.DATABASE_ERROR, ErrorCode.DATABASE_ERROR.getMessage()));

        mockMvc.perform(delete("/api/v1/posts/1/comments/1")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser
    @DisplayName("댓글 삭제 실패(5) : Post 없는 경우")
    void delete_fail5() throws Exception {
        when(commentService.delete(any(), any(), any())).thenThrow(new LikeLionException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        mockMvc.perform(delete("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}