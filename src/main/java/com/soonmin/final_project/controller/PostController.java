package com.soonmin.final_project.controller;

import com.soonmin.final_project.domain.Response;
import com.soonmin.final_project.domain.dto.comment.CommentCreateRequest;
import com.soonmin.final_project.domain.dto.comment.CommentDto;
import com.soonmin.final_project.domain.dto.comment.CommentResponse;
import com.soonmin.final_project.domain.dto.post.*;
import com.soonmin.final_project.service.CommentService;
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
    private final CommentService commentService;

    // 포스트 등록
    @PostMapping
    public Response<PostCreateResponse> createPost (@RequestBody PostCreateRequest request, Authentication authentication) {
        String userName = authentication.getName();
        PostDto postDto = postService.create(request, userName);
        return Response.success(postDto.toCreateResponse());
    }

    // 포스트 리스트 보기
    @GetMapping
    public Response<PostList> viewList(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        List<PostViewResponse> postList = postService.viewList(pageable);
        return Response.success(new PostList(postList, pageable));
    }

    // 포스트 디테일 보기
    @GetMapping("/{id}")
    public Response<PostViewResponse> viewDetail(@PathVariable Integer id) {
        PostDto postDto = postService.viewDetail(id);
        return Response.success(postDto.toViewResponse());
    }

    // 포스트 삭제
    @DeleteMapping("/{id}")
    public Response<PostDeleteResponse> deletePost(@PathVariable Integer id, Authentication authentication) {
        Integer deletedPostId = postService.delete(id, authentication.getName());
        return Response.success(new PostDeleteResponse("포스트 삭제 완료", deletedPostId));
    }

    // 포스트 수정
    @PutMapping("/{id}")
    public Response<PostUpdateResponse> updatePost(@PathVariable Integer id, Authentication authentication, PostUpdateRequest request) {
        Integer updatedPostId = postService.update(id, authentication.getName(), request);
        return Response.success(new PostUpdateResponse("포스트 수정 완료", updatedPostId));
    }

    // 댓글 작성
    @PostMapping("/{postId}/comments")
    public Response<CommentResponse> createComment(@PathVariable Integer postId, Authentication authentication, CommentCreateRequest request) {
        CommentDto commentDto = commentService.create(postId, authentication.getName(), request);
        return Response.success(commentDto.toResponse());
    }
}
