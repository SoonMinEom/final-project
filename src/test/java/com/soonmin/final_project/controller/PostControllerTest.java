package com.soonmin.final_project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soonmin.final_project.domain.dto.post.PostCreateRequest;
import com.soonmin.final_project.domain.dto.post.PostDto;
import com.soonmin.final_project.domain.dto.post.PostUpdateRequest;
import com.soonmin.final_project.exception.ErrorCode;
import com.soonmin.final_project.exception.LikeLionException;
import com.soonmin.final_project.service.PostService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
@MockBean(JpaMetamodelMappingContext.class)
class PostControllerTest {
    
    @MockBean
    PostService postService;
    
    @Autowired
    ObjectMapper objectMapper;
    
    @Autowired
    MockMvc mockMvc;

    PostCreateRequest postCreateRequest = new PostCreateRequest("test", "test");

    PostDto postDto = PostDto.builder()
            .id(1)
            .title("test")
            .body("test")
            .build();


    @Test
    @WithMockUser
    @DisplayName("포스트 작성 성공")
    void createPost_success() throws Exception {
        when(postService.create(any(), any())).thenReturn(PostDto.builder().id(1).title("test").body("test").build());

        mockMvc.perform(post("/api/v1/posts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postCreateRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.message").value("포스트 등록 완료"));
    }

    @Test
    @WithAnonymousUser
    @DisplayName("포스트 작성 실패 - 인증 실패 : 로그인 하지 않은 경우")
    void createPost_fail() throws Exception {
        when(postService.create(any(), any())).thenThrow(new LikeLionException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(post("/api/v1/posts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postCreateRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    @DisplayName("포스트 조회 성공")
    void viewPost_success() throws Exception {
        when(postService.viewDetail(1)).thenReturn(postDto);

        mockMvc.perform(get("/api/v1/posts/1").with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.id").value(postDto.getId()))
                .andExpect(jsonPath("$.result.title").value(postDto.getTitle()))
                .andExpect(jsonPath("$.result.body").value(postDto.getBody()))
                .andExpect(jsonPath("$.result.userName").value(postDto.getUserName()));
    }

    @Test
    @WithMockUser
    @DisplayName("포스트 수정 성공")
    void updatePost_success() throws Exception {
        PostUpdateRequest postUpdateRequest = new PostUpdateRequest("수정", "완료");
        when(postService.update(any(), any(), any())).thenReturn(1);

        mockMvc.perform(put("/api/v1/posts/1")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(postUpdateRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.message").value("포스트 수정 완료"))
                .andExpect(jsonPath("$.result.postId").value(1));
    }

    @Test
    @WithAnonymousUser
    @DisplayName("포스트 수정 실패 - 인증실패")
    void updatePost_fail1() throws Exception {

        PostUpdateRequest postUpdateRequest = new PostUpdateRequest("수정", "실패");

        when(postService.update(any(), any(), any()))
                .thenThrow(new LikeLionException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(put("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postUpdateRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getStatus().value()));
    }

    @Test
    @WithMockUser
    @DisplayName("포스트 수정 실패 - 작성자 불일치")
    void updatePost_fail2() throws Exception {

        PostUpdateRequest postUpdateRequest = new PostUpdateRequest("수정", "실패");

        when(postService.update(any(), any(), any()))
                .thenThrow(new LikeLionException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(put("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postUpdateRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getStatus().value()))
                .andExpect(jsonPath("$.result.errorCode").value("INVALID_PERMISSION"))
                .andExpect(jsonPath("$.result.message").value("사용자가 권한이 없습니다."));
    }

    @Test
    @WithMockUser
    @DisplayName("포스트 수정 실패 - 데이터베이스 에러")
    void updatePost_fail3() throws Exception {

        PostUpdateRequest postUpdateRequest = new PostUpdateRequest("수정", "실패");

        when(postService.update(any(), any(), any()))
                .thenThrow(new LikeLionException(ErrorCode.DATABASE_ERROR, ErrorCode.DATABASE_ERROR.getMessage()));

        mockMvc.perform(put("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postUpdateRequest)))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.result.errorCode").value("DATABASE_ERROR"))
                .andExpect(jsonPath("$.result.message").value("DB에러"));
    }

    @Test
    @WithMockUser
    @DisplayName("포스트 삭제 성공")
    void deletePost_success() throws Exception {

        when(postService.delete(any(), any())).thenReturn(1);

        mockMvc.perform(delete("/api/v1/posts/1").with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.message").value("포스트 삭제 완료"))
                .andExpect(jsonPath("$.result.postId").value(1));
    }

    @Test
    @WithAnonymousUser
    @DisplayName("포스트 삭제 실패 - 인증실패")
    void deletePost_fail1() throws Exception {

        when(postService.delete(any(), any()))
                .thenThrow(new LikeLionException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(delete("/api/v1/posts/1").with(csrf()))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    @DisplayName("포스트 삭제 실패 - 작성자 불일치")
    void deletePost_fail2() throws Exception {

        when(postService.delete(any(), any()))
                .thenThrow(new LikeLionException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(delete("/api/v1/posts/1").with(csrf()))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.result.errorCode").value("INVALID_PERMISSION"))
                .andExpect(jsonPath("$.result.message").value("사용자가 권한이 없습니다."));
    }

    @Test
    @WithMockUser
    @DisplayName("포스트 삭제 실패 - 데이터베이스 에러")
    void deletePost_fail3() throws Exception {

        when(postService.delete(any(), any())).thenThrow(new LikeLionException(ErrorCode.DATABASE_ERROR, ErrorCode.DATABASE_ERROR.getMessage()));

        mockMvc.perform(delete("/api/v1/posts/1")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.result.errorCode").value("DATABASE_ERROR"))
                .andExpect(jsonPath("$.result.message").value("DB에러"));
    }
}