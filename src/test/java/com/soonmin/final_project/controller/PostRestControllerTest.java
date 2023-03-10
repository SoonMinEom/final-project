package com.soonmin.final_project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soonmin.final_project.domain.dto.post.PostCreateRequest;
import com.soonmin.final_project.domain.dto.post.PostDto;
import com.soonmin.final_project.domain.dto.post.PostUpdateRequest;
import com.soonmin.final_project.domain.dto.post.PostViewResponse;
import com.soonmin.final_project.exception.ErrorCode;
import com.soonmin.final_project.exception.LikeLionException;
import com.soonmin.final_project.service.CommentService;
import com.soonmin.final_project.service.LikeService;
import com.soonmin.final_project.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostRestController.class)
@MockBean(JpaMetamodelMappingContext.class)
class PostRestControllerTest {
    
    @MockBean
    PostService postService;
    @MockBean
    CommentService commentService;
    @MockBean
    LikeService likeService;
    
    @Autowired
    ObjectMapper objectMapper;
    
    @Autowired
    MockMvc mockMvc;

    PostCreateRequest postCreateRequest;
    PostDto postDto;
    Page<PostViewResponse> postPage;

    @BeforeEach
    void beforeEach() {
        postCreateRequest = new PostCreateRequest("test", "test");
        postDto = PostDto.builder()
                .id(1)
                .title("test")
                .body("test")
                .build();

        List<PostViewResponse> postList = new ArrayList<>();
        postList.add(new PostViewResponse(1, "title1", "body1","userName1", LocalDateTime.now(),LocalDateTime.now()));
        postList.add(new PostViewResponse(2, "title2", "body2","userName2", LocalDateTime.now(),LocalDateTime.now()));
        postList.add(new PostViewResponse(3, "title3", "body3","userName3", LocalDateTime.now(),LocalDateTime.now()));

        postPage = new PageImpl<>(postList);
    }


    @Test
    @WithMockUser
    @DisplayName("????????? ?????? ??????")
    void createPost_success() throws Exception {
        when(postService.create(any(), any())).thenReturn(PostDto.builder().id(1).title("test").body("test").build());

        mockMvc.perform(post("/api/v1/posts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postCreateRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.message").value("????????? ?????? ??????"));
    }

    @Test
    @WithAnonymousUser
    @DisplayName("????????? ?????? ?????? - ?????? ?????? : ????????? ?????? ?????? ??????")
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
    @DisplayName("????????? ?????? ??????")
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
    @DisplayName("????????? ????????? ?????? ??????")
    void viewList_success() throws Exception {
        when(postService.viewList(any())).thenReturn(postPage);

        mockMvc.perform(get("/api/v1/posts").with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result.pageable").exists());

    }

    @Test
    @WithMockUser
    @DisplayName("????????? ?????? ??????")
    void updatePost_success() throws Exception {
        PostUpdateRequest postUpdateRequest = new PostUpdateRequest("??????", "??????");
        when(postService.update(any(), any(), any())).thenReturn(1);

        mockMvc.perform(put("/api/v1/posts/1")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(postUpdateRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.message").value("????????? ?????? ??????"))
                .andExpect(jsonPath("$.result.postId").value(1));
    }

    @Test
    @WithAnonymousUser
    @DisplayName("????????? ?????? ?????? - ????????????")
    void updatePost_fail1() throws Exception {

        PostUpdateRequest postUpdateRequest = new PostUpdateRequest("??????", "??????");

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
    @DisplayName("????????? ?????? ?????? - ????????? ?????????")
    void updatePost_fail2() throws Exception {

        PostUpdateRequest postUpdateRequest = new PostUpdateRequest("??????", "??????");

        when(postService.update(any(), any(), any()))
                .thenThrow(new LikeLionException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(put("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postUpdateRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getStatus().value()))
                .andExpect(jsonPath("$.result.errorCode").value("INVALID_PERMISSION"))
                .andExpect(jsonPath("$.result.message").value("???????????? ????????? ????????????."));
    }

    @Test
    @WithMockUser
    @DisplayName("????????? ?????? ?????? - ?????????????????? ??????")
    void updatePost_fail3() throws Exception {

        PostUpdateRequest postUpdateRequest = new PostUpdateRequest("??????", "??????");

        when(postService.update(any(), any(), any()))
                .thenThrow(new LikeLionException(ErrorCode.DATABASE_ERROR, ErrorCode.DATABASE_ERROR.getMessage()));

        mockMvc.perform(put("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postUpdateRequest)))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.result.errorCode").value("DATABASE_ERROR"))
                .andExpect(jsonPath("$.result.message").value("DB??????"));
    }

    @Test
    @WithMockUser
    @DisplayName("????????? ?????? ??????")
    void deletePost_success() throws Exception {

        when(postService.delete(any(), any())).thenReturn(1);

        mockMvc.perform(delete("/api/v1/posts/1").with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.message").value("????????? ?????? ??????"))
                .andExpect(jsonPath("$.result.postId").value(1));
    }

    @Test
    @WithAnonymousUser
    @DisplayName("????????? ?????? ?????? - ????????????")
    void deletePost_fail1() throws Exception {

        when(postService.delete(any(), any()))
                .thenThrow(new LikeLionException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(delete("/api/v1/posts/1").with(csrf()))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    @DisplayName("????????? ?????? ?????? - ????????? ?????????")
    void deletePost_fail2() throws Exception {

        when(postService.delete(any(), any()))
                .thenThrow(new LikeLionException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(delete("/api/v1/posts/1").with(csrf()))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.result.errorCode").value("INVALID_PERMISSION"))
                .andExpect(jsonPath("$.result.message").value("???????????? ????????? ????????????."));
    }

    @Test
    @WithMockUser
    @DisplayName("????????? ?????? ?????? - ?????????????????? ??????")
    void deletePost_fail3() throws Exception {

        when(postService.delete(any(), any())).thenThrow(new LikeLionException(ErrorCode.DATABASE_ERROR, ErrorCode.DATABASE_ERROR.getMessage()));

        mockMvc.perform(delete("/api/v1/posts/1")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.result.errorCode").value("DATABASE_ERROR"))
                .andExpect(jsonPath("$.result.message").value("DB??????"));
    }

    @Test
    @WithMockUser
    @DisplayName("???????????? ?????? ??????")
    void myFeed_success() throws Exception {

        when(postService.myFeed(any(), any())).thenReturn(postPage);

        mockMvc.perform(get("/api/v1/posts/my").with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result.pageable").exists());
    }

    @Test
    @WithAnonymousUser
    @DisplayName("???????????? ?????? ?????? - ????????? ?????? ?????? ??????")
    void myFeed_fail() throws Exception {

        when(postService.myFeed(any(), any())).thenThrow(new LikeLionException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));

        mockMvc.perform(get("/api/v1/posts/my").with(csrf()))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    @DisplayName("????????? ????????? ??????")
    void like_success() throws Exception {
        when(likeService.like(any(),any())).thenReturn(1);

        mockMvc.perform(post("/api/v1/posts/1/likes").with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("???????????? ???????????????."));
    }

    @Test
    @WithAnonymousUser
    @DisplayName("????????? ????????? ??????(1) - ????????? ?????? ?????? ??????")
    void like_fail1() throws Exception {
        when(likeService.like(any(),any())).thenThrow(new LikeLionException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));

        mockMvc.perform(post("/api/v1/posts/1/likes").with(csrf()))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    @DisplayName("????????? ????????? ??????(2) - ?????? ???????????? ?????? ??????")
    void like_fail2() throws Exception {
        when(likeService.like(any(),any())).thenThrow(new LikeLionException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        mockMvc.perform(post("/api/v1/posts/1/likes").with(csrf()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}