package com.soonmin.final_project.service;

import com.soonmin.final_project.domain.dto.post.PostCreateRequest;
import com.soonmin.final_project.domain.dto.post.PostDto;
import com.soonmin.final_project.domain.dto.post.PostUpdateRequest;
import com.soonmin.final_project.domain.dto.post.PostViewResponse;
import com.soonmin.final_project.domain.entity.Post;
import com.soonmin.final_project.domain.entity.User;
import com.soonmin.final_project.exception.ErrorCode;
import com.soonmin.final_project.exception.LikeLionException;
import com.soonmin.final_project.repository.PostRepository;
import com.soonmin.final_project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    // 포스트 작성
    public PostDto create(PostCreateRequest request, String userName) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new LikeLionException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));
        Post post = postRepository.save(request.toEntity(user));
        return post.toDto();
    }

    // 포스트 디테일 보기
    public PostDto viewDetail(Integer id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new LikeLionException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));
        return post.toDto();
    }

    // 포스트 리스트 보기
    @Transactional
    public Page<PostViewResponse> viewList(Pageable pageable) {
        Page<Post> postPage = postRepository.findAll(pageable);
        return postPage.map(post -> post.toDto().toViewResponse());
    }

    // 포스트 삭제
    public Integer delete(Integer id, String userName) {
        Post post = postRepository.findById(id)
                .orElseThrow(()->new LikeLionException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        userRepository.findByUserName(userName)
                .orElseThrow(() -> new LikeLionException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));

        log.info("userName get at post : {}", post.getUser().getUserName());
        log.info("userName get at authentication : {}", userName);
        if (!post.getUser().getUserName().equals(userName)){
            throw new LikeLionException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
        }

        postRepository.deleteById(id);

        return id;
    }

    // 포스트 수정
    public Integer update(Integer id, String userName, PostUpdateRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(()->new LikeLionException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        userRepository.findByUserName(userName)
                .orElseThrow(() -> new LikeLionException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));

        if (!post.getUser().getUserName().equals(userName)){
            throw new LikeLionException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
        }

        post.update(request.getTitle(), request.getBody());
        postRepository.save(post);

        return id;
    }

    // 마이 피드
    public Page<PostViewResponse> myFeed(String userName, Pageable pageable) {
        // 유저가 존재하는지 검증(로그인)
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new LikeLionException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));

        // 유저 정보로 포스트 검색
        Page<Post> postPage = postRepository.findByUser(user, pageable);

        return postPage.map(post -> post.toDto().toViewResponse());
    }
}
