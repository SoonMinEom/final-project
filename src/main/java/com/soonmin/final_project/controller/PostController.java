package com.soonmin.final_project.controller;

import com.soonmin.final_project.domain.Response;
import com.soonmin.final_project.domain.dto.PostCreateRequest;
import com.soonmin.final_project.domain.dto.PostCreateResponse;
import com.soonmin.final_project.domain.dto.PostDto;
import com.soonmin.final_project.domain.dto.PostViewResponse;
import com.soonmin.final_project.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public Response<PostViewResponse> viewDetail(@PathVariable Integer id) {
        PostDto postDto = postService.viewDetail(id);
        return Response.success(postDto.toViewResponse());
    }
}
