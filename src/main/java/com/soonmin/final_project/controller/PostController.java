package com.soonmin.final_project.controller;

import com.soonmin.final_project.domain.Response;
import com.soonmin.final_project.domain.dto.comment.CommentDeleteResponse;
import com.soonmin.final_project.domain.dto.comment.CommentRequest;
import com.soonmin.final_project.domain.dto.comment.CommentDto;
import com.soonmin.final_project.domain.dto.comment.CommentResponse;
import com.soonmin.final_project.domain.dto.post.*;
import com.soonmin.final_project.domain.entity.Like;
import com.soonmin.final_project.service.CommentService;
import com.soonmin.final_project.service.LikeService;
import com.soonmin.final_project.service.PostService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;
    private final CommentService commentService;
    private final LikeService likeService;

    // 포스트 등록
    @PostMapping
    @ApiOperation(value = "게시글 등록", notes = "게시글을 등록합니다. 로그인한 사용자만 게시글을 등록할 수 있습니다.")
    public Response<PostCreateResponse> createPost (@RequestBody PostCreateRequest request, @ApiIgnore Authentication authentication) {
        String userName = authentication.getName();
        PostDto postDto = postService.create(request, userName);
        return Response.success(postDto.toCreateResponse());
    }

    // 포스트 리스트 보기
    @GetMapping
    @ApiOperation( value = "게시글 목록 조회", notes = "게시글 목록을 20개 단위로 조회합니다. 로그인하지 않아도 조회할 수 있습니다.")
    public Response<Page<PostViewResponse>> viewList(@ApiIgnore @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostViewResponse> postPage = postService.viewList(pageable);
        return Response.success(postPage);
    }

    // 포스트 디테일 보기
    @GetMapping("/{id}")
    @ApiOperation(value = "게시글 내용 조회", notes = "게시글의 내용을 조회합니다. 로그인하지 않아도 조회할 수 있습니다.")
    @ApiImplicitParam(name = "id", value = "게시글 번호", defaultValue = "None")
    public Response<PostViewResponse> viewDetail(@PathVariable Integer id) {
        PostDto postDto = postService.viewDetail(id);
        return Response.success(postDto.toViewResponse());
    }

    // 포스트 삭제
    @DeleteMapping("/{id}")
    @ApiOperation(value = "게시글 삭제", notes = "게시글을 삭제합니다. 해당 게시글을 작성한 사용자만 삭제할 수 있습니다.")
    @ApiImplicitParam(name = "id", value = "게시글 번호", defaultValue = "None")
    public Response<PostDeleteResponse> deletePost(@PathVariable Integer id, @ApiIgnore Authentication authentication) {
        Integer deletedPostId = postService.delete(id, authentication.getName());
        return Response.success(new PostDeleteResponse("포스트 삭제 완료", deletedPostId));
    }

    // 포스트 수정
    @PutMapping("/{id}")
    @ApiOperation(value = "게시글 수정", notes = "게시글을 수정합니다. 해당 게시글을 작성한 사용자만 수정할 수 있습니다.")
    @ApiImplicitParam(name = "id", value = "게시글 번호", defaultValue = "None")
    public Response<PostUpdateResponse> updatePost(@PathVariable Integer id, @ApiIgnore Authentication authentication, @RequestBody PostUpdateRequest request) {
        Integer updatedPostId = postService.update(id, authentication.getName(), request);
        return Response.success(new PostUpdateResponse("포스트 수정 완료", updatedPostId));
    }

    // 댓글 작성
    @PostMapping("/{postId}/comments")
    @ApiOperation(value = "댓글 등록", notes = "댓글을 등록합니다. 로그인한 사용자만 댓글을 등록할 수 있습니다.")
    public Response<CommentResponse> createComment(@PathVariable Integer postId, @ApiIgnore Authentication authentication, @RequestBody CommentRequest request) {
        CommentDto commentDto = commentService.create(postId, authentication.getName(), request);
        return Response.success(commentDto.toResponse());
    }

    // 댓글 조회
    @GetMapping("/{postId}/comments")
    @ApiOperation(value = "댓글 조회", notes = "특정 게시글에 달린 댓글을 10개 단위로 조회합니다. 로그인하지 않아도 조회할 수 있습니다.")
    public Response<Page<CommentResponse>> viewComment(@PathVariable Integer postId, @ApiIgnore @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<CommentResponse> commentPage  = commentService.view(postId, pageable);
        return Response.success(commentPage);
    }

    // 댓글 수정
    @PutMapping("/{postId}/comments/{id}")
    @ApiOperation(value = "댓글 수정", notes = "댓글을 수정합니다. 해당 댓글을 작성한 사용자만 댓글을 수정할 수 있습니다.")
    public Response<CommentResponse> updateComment(@PathVariable Integer postId, @PathVariable Integer id, @RequestBody CommentRequest request, @ApiIgnore Authentication authentication) {
        CommentDto commentDto = commentService.update(postId, id, request, authentication.getName());
        return Response.success(commentDto.toResponse());
    }

    // 댓글 삭제
    @DeleteMapping("/{postId}/comments/{id}")
    @ApiOperation(value = "댓글 삭제", notes = "댓글을 삭제합니다. 해당 댓글을 작성한 사용자만 댓글을 삭제할 수 있습니다.")
    public Response<CommentDeleteResponse> deleteComment(@PathVariable Integer postId, @PathVariable Integer id, @ApiIgnore Authentication authentication) {
        Integer deletedCommentId = commentService.delete(postId, id, authentication.getName());
        return Response.success(new CommentDeleteResponse("댓글 삭제 완료", deletedCommentId));
    }

    // 마이 피드
    @GetMapping("/my")
    @ApiOperation(value = "자신의 게시글 목록 조회", notes = "자신이 작성한 게시글의 목록을 조회합니다. 로그인한 사용자만 조회할 수 있습니다.")
    public Response<Page<PostViewResponse>> myFeed(@ApiIgnore Authentication authentication, @ApiIgnore @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostViewResponse> postPage = postService.myFeed(authentication.getName(), pageable);
        return Response.success(postPage);
    }

    // 좋아요 누르기
    @PostMapping("/{postId}/likes")
    @ApiOperation(value = "좋아요 누르기", notes = "좋아요를 누릅니다. 이미 좋아요를 누른 경우 좋아요를 취소합니다. 로그인한 사용자만 좋아요를 누를 수 있습니다.")
    public Response<String> like(@PathVariable Integer postId, @ApiIgnore Authentication authentication) {
        Integer result = likeService.like(postId, authentication.getName());
        if (result == 0) return Response.success("좋아요를 취소했습니다.");
        else return Response.success("좋아요를 눌렀습니다.");
    }

    // 좋아요 개수
    @GetMapping("/{postId}/likes")
    @ApiOperation(value = "좋아요 개수 조회", notes = "특정 게시글의 좋아요 개수를 조회합니다. 로그인하지 않아도 조회할 수 있습니다.")
    public Response<Integer> numOfLikes(@PathVariable Integer postId) {
        Integer result = likeService.numOfLikes(postId);
        return Response.success(result);
    }
}
