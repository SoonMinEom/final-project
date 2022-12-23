package com.soonmin.final_project.controller;

import com.soonmin.final_project.domain.Response;
import com.soonmin.final_project.domain.dto.*;
import com.soonmin.final_project.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;

    @PostMapping
    public Response<PostCreateResponse> createPost (@RequestBody PostCreateRequest request, Authentication authentication) {
        PostDto postDto = postService.create(request, authentication);
        return Response.success(postDto.toCreateResponse());
    }
    @GetMapping
    public Response<PostList> viewList(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        List<PostViewResponse> postList = postService.viewList(pageable);
        return Response.success(new PostList(postList, (postList.size()/20)+1));
    }
    @GetMapping("/{id}")
    public Response<PostViewResponse> viewDetail(@PathVariable Integer id) {
        PostDto postDto = postService.viewDetail(id);
        return Response.success(postDto.toViewResponse());
    }
    @DeleteMapping("/{id}")
    public Response<PostDeleteResponse> deletePost(@PathVariable Integer id, Authentication authentication) {
        Integer deletedPostId = postService.delete(id, authentication);
        return Response.success(new PostDeleteResponse("포스트 삭제 완료", deletedPostId));
    }

    @PutMapping("/{id}")
    public Response<PostUpdateResponse> updatePost(@PathVariable Integer id, Authentication authentication, PostUpdateRequest request) {
        Integer updatedPostId = postService.update(id, authentication, request);
        return Response.success(new PostUpdateResponse("포스트 수정 완료", updatedPostId));
    }
}
