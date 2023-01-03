package com.soonmin.final_project.service;

import com.soonmin.final_project.domain.dto.comment.CommentRequest;
import com.soonmin.final_project.domain.dto.comment.CommentDto;
import com.soonmin.final_project.domain.dto.comment.CommentResponse;
import com.soonmin.final_project.domain.entity.Comment;
import com.soonmin.final_project.domain.entity.Post;
import com.soonmin.final_project.domain.entity.User;
import com.soonmin.final_project.exception.ErrorCode;
import com.soonmin.final_project.exception.LikeLionException;
import com.soonmin.final_project.repository.CommentRepository;
import com.soonmin.final_project.repository.PostRepository;
import com.soonmin.final_project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    // 댓글 작성
    public CommentDto create(Integer postId, String userName, CommentRequest request) {
        // 로그인 한 사용자인지 검증
        User user = userRepository.findByUserName(userName)
                .orElseThrow(()->new LikeLionException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));

        // 포스트가 존재하는지 검증
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new LikeLionException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        // 코맨트 작성
        Comment comment = commentRepository.save(request.toEntity(user, post));

        //Dto 리턴
        return comment.toDto();
    }

    public Page<CommentResponse> view (Integer postId, Pageable pageable) {
        // 포스트가 존재하는지 검증
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new LikeLionException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        // post 로 comment 검색, comment 를 commentResponse 로 변환
        Page<CommentResponse> commentPage = commentRepository.findByPost(post, pageable)
                .map(comment -> comment.toDto().toResponse());

        return commentPage;
    }

    public CommentDto update(Integer postId, Integer id, CommentRequest request, String userName) {
        // 포스트가 존재하는지 검증
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new LikeLionException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        // 댓글이 존재하는지 검증
        Comment comment = commentRepository.findById(id)
                .orElseThrow(()-> new LikeLionException(ErrorCode.COMMENT_NOT_FOUND, ErrorCode.COMMENT_NOT_FOUND.getMessage()));

        // 댓글의 작성자와 수정 요청한 User 가 같은지 검증
        if (!comment.getUser().getUserName().equals(userName)) {
            throw new LikeLionException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
        }

        // 댓글 수정
        comment.update(request.getComment());
        Comment updatedComment = commentRepository.save(comment);

        return updatedComment.toDto();
    }
}
