package com.soonmin.final_project.service;

import com.soonmin.final_project.domain.dto.comment.CommentCreateRequest;
import com.soonmin.final_project.domain.dto.comment.CommentDto;
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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    // 댓글 작성
    public CommentDto create(Integer postId, String userName, CommentCreateRequest request) {
        // 로그인 한 사용자인지 검증
        User user = userRepository.findByUserName(userName)
                .orElseThrow(()->new LikeLionException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));

        // 포스트가 존재하는 지 검증
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new LikeLionException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        // 코맨트 작성
        Comment comment = commentRepository.save(request.toEntity(user, post));

        //Dto 리턴
        return comment.toDto();
    }
}
