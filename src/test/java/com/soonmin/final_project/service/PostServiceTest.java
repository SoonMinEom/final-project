package com.soonmin.final_project.service;

import com.soonmin.final_project.domain.UserRole;
import com.soonmin.final_project.domain.dto.post.PostCreateRequest;
import com.soonmin.final_project.domain.dto.post.PostDto;
import com.soonmin.final_project.domain.dto.post.PostUpdateRequest;
import com.soonmin.final_project.domain.entity.Post;
import com.soonmin.final_project.domain.entity.User;
import com.soonmin.final_project.exception.ErrorCode;
import com.soonmin.final_project.exception.LikeLionException;
import com.soonmin.final_project.repository.PostRepository;
import com.soonmin.final_project.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


class PostServiceTest {
    private final PostRepository postRepository = mock(PostRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);

    PostService postService;
    User user;
    PostCreateRequest postCreateRequest;
    PostUpdateRequest postUpdateRequest;
    Post post;


    @BeforeEach
    void setUp() {
        postService = new PostService(postRepository, userRepository);
        user = new User(1, "test", "test", UserRole.USER);
        postCreateRequest = new PostCreateRequest("title", "body");
        postUpdateRequest = new PostUpdateRequest("update","update");
        post = Post.builder()
                .id(1)
                .title(postCreateRequest.getTitle())
                .body(postCreateRequest.getBody())
                .user(user)
                .build();
    }

    @Test
    @DisplayName("포스트 등록 성공")
    void postCreate_success() {
        User mockUser = mock(User.class);
        Post mockPost = mock(Post.class);

        Mockito.when(userRepository.findByUserName(user.getUserName())).thenReturn(Optional.of(user));
        Mockito.when(postRepository.save(postCreateRequest.toEntity(any()))).thenReturn(post);

        Assertions.assertDoesNotThrow(() -> postService.create(postCreateRequest, user.getUserName()));
    }

    @Test
    @DisplayName("포스트 등록 실패 : 유저가 존재하지 않을 때")
    void postCreate_fail() {
        Mockito.when(userRepository.findByUserName(user.getUserName())).thenReturn(Optional.empty());
        Mockito.when(postRepository.save(postCreateRequest.toEntity(any()))).thenReturn(post);

        String errorMessage = Assertions.assertThrows(LikeLionException.class, () -> postService.create(postCreateRequest, user.getUserName())).getMessage();

        Assertions.assertEquals(ErrorCode.USERNAME_NOT_FOUND.getMessage(), errorMessage);
        System.out.printf("%s = %s",ErrorCode.USERNAME_NOT_FOUND.getMessage(), errorMessage);
    }

    @Test
    @DisplayName("포스트 조회 성공")
    void viewDetail_success() {
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));

        PostDto postDto = postService.viewDetail(post.getId());

        Assertions.assertEquals("test",postDto.getUserName());
    }

    @Test
    @DisplayName("포스트 수정 실패 1 : 포스트 존재하지 않음")
    void updatePost_fail1() {
        when(postRepository.findById(post.getId())).thenReturn(Optional.empty());

        String errorMessage = Assertions.assertThrows(LikeLionException.class, ()->postService.update(post.getId(), user.getUserName(), postUpdateRequest)).getMessage();

        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND.getMessage(), errorMessage);
    }

    @Test
    @DisplayName("포스트 수정 실패 2 : 작성자 != 유저")
    void updatePost_fail2() {
        User user2 = new User(2, "test2", "test2", UserRole.USER);

        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(userRepository.findByUserName(user2.getUserName())).thenReturn(Optional.of(user));

        String errorMessage = Assertions.assertThrows(LikeLionException.class, () -> postService.update(post.getId(), user2.getUserName(), postUpdateRequest)).getMessage();

        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION.getMessage(), errorMessage);
    }

    @Test
    @DisplayName("포스트 수정 실패 3 : 유저 존재하지 않음")
    void updatePost_fail3() {
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(userRepository.findByUserName(user.getUserName())).thenReturn(Optional.empty());

        String errorMessage = Assertions.assertThrows(LikeLionException.class, ()->postService.update(post.getId(), user.getUserName(), postUpdateRequest)).getMessage();

        Assertions.assertEquals(ErrorCode.USERNAME_NOT_FOUND.getMessage(), errorMessage);
    }


    @Test
    @DisplayName("포스트 삭제 실패 1 : 유저 존재하지 않음")
    void deletePost_fail1() {
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(userRepository.findByUserName(user.getUserName())).thenReturn(Optional.empty());

        String errorMessage = Assertions.assertThrows(LikeLionException.class,()->postService.delete(post.getId(), user.getUserName())).getMessage();

        Assertions.assertEquals(ErrorCode.USERNAME_NOT_FOUND.getMessage(),errorMessage );
    }

    @Test
    @DisplayName("포스트 삭제 실패 2 :  포스트 존재하지 않음")
    void deletePost_fail2() {
        when(postRepository.findById(post.getId())).thenReturn(Optional.empty());

        String errorMessage = Assertions.assertThrows(LikeLionException.class, () -> postService.delete(post.getId(), user.getUserName())).getMessage();

        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND.getMessage(),errorMessage );
    }

    @Test
    @DisplayName("포스트 삭제 실패 3 : 작성자와 유저가 같지 않음")
    void name() {
        User user2 = new User(2, "test2", "test2", UserRole.USER);

        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(userRepository.findByUserName(user2.getUserName())).thenReturn(Optional.of(user2));

        String errorMessage = Assertions.assertThrows(LikeLionException.class, () -> postService.delete(post.getId(), user2.getUserName())).getMessage();
        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION.getMessage(),errorMessage );
    }
}