package com.soonmin.final_project.service;

import com.soonmin.final_project.domain.UserRole;
import com.soonmin.final_project.domain.dto.comment.CommentRequest;
import com.soonmin.final_project.domain.entity.Comment;
import com.soonmin.final_project.domain.entity.Post;
import com.soonmin.final_project.domain.entity.User;
import com.soonmin.final_project.exception.ErrorCode;
import com.soonmin.final_project.exception.LikeLionException;
import com.soonmin.final_project.repository.AlarmRepository;
import com.soonmin.final_project.repository.CommentRepository;
import com.soonmin.final_project.repository.PostRepository;
import com.soonmin.final_project.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CommentServiceTest {
    private final CommentRepository commentRepository = mock(CommentRepository.class);
    private final PostRepository postRepository = mock(PostRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final AlarmRepository alarmRepository = mock(AlarmRepository.class);

    CommentService commentService;
    User user;
    User user2;
    Post post;
    Comment comment;
    CommentRequest commentRequest;

    @BeforeEach
    void beforeEach() {
        commentService = new CommentService(commentRepository, userRepository, postRepository, alarmRepository);
        user = new User(1, "userName", "password", UserRole.USER);
        user2 = new User(2, "userName2", "password2", UserRole.USER);
        post = new Post(1, "title", "body", user, LocalDateTime.now());
        comment = new Comment(1, "comment", post, user, LocalDateTime.now());
        commentRequest = new CommentRequest("updated comment");
    }

    @Test
    @DisplayName("댓글 수정 실패(1) : 포스트 존재하지 않음")
    void update_fail1() {
        Mockito.when(postRepository.findById(post.getId())).thenReturn(Optional.empty());

        String errorMessage = Assertions.assertThrows(LikeLionException.class, () -> commentService.update(post.getId(), comment.getId(), commentRequest, comment.getUser().getUserName())).getMessage();

        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND.getMessage(), errorMessage);
    }

    @Test
    @DisplayName("댓글 수정 실패(2) : 작성자!=유저")
    void update_fail2() {
        Mockito.when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        Mockito.when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
        Mockito.when(userRepository.findById(comment.getUser().getId())).thenReturn(Optional.of(user));

        String errorMessage = Assertions.assertThrows(LikeLionException.class, () -> commentService.update(post.getId(), comment.getId(), commentRequest, user2.getUserName())).getMessage();

        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION.getMessage(), errorMessage);
    }

    @Test
    @DisplayName("댓글 수정 실패(3) : 유저 존재하지 않음")
    void update_fail3() {
        Mockito.when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        Mockito.when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
        Mockito.when(userRepository.findById(comment.getUser().getId())).thenReturn(Optional.empty());

        String errorMessage = Assertions.assertThrows(LikeLionException.class, () -> commentService.update(post.getId(), comment.getId(), commentRequest, comment.getUser().getUserName())).getMessage();

        Assertions.assertEquals(ErrorCode.USERNAME_NOT_FOUND.getMessage(), errorMessage);
    }

    @Test
    @DisplayName("댓글 삭제 실패(1) : 유저 존재하지 않음")
    void delete_fail1() {
        Mockito.when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        Mockito.when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
        Mockito.when(userRepository.findById(comment.getUser().getId())).thenReturn(Optional.empty());

        String errorMessage = Assertions.assertThrows(LikeLionException.class, () -> commentService.delete(post.getId(), comment.getId(), comment.getUser().getUserName())).getMessage();

        Assertions.assertEquals(ErrorCode.USERNAME_NOT_FOUND.getMessage(), errorMessage);
    }

    @Test
    @DisplayName("댓글 삭제 실패 (2): 댓글이 존재하지 않음")
    void delete_fail2() {
        Mockito.when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        Mockito.when(commentRepository.findById(comment.getId())).thenReturn(Optional.empty());

        String errorMessage = Assertions.assertThrows(LikeLionException.class, () -> commentService.delete(post.getId(), comment.getId(), comment.getUser().getUserName())).getMessage();

        Assertions.assertEquals(ErrorCode.COMMENT_NOT_FOUND.getMessage(), errorMessage);
    }

    @Test
    @DisplayName("댓글 수정 실패(3) : 작성자!=유저")
    void delete_fail3() {
        Mockito.when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        Mockito.when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
        Mockito.when(userRepository.findById(comment.getUser().getId())).thenReturn(Optional.of(user));

        String errorMessage = Assertions.assertThrows(LikeLionException.class, () -> commentService.delete(post.getId(), comment.getId(), user2.getUserName())).getMessage();

        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION.getMessage(), errorMessage);
    }
}