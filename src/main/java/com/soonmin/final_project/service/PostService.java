package com.soonmin.final_project.service;

import com.soonmin.final_project.domain.dto.PostCreateRequest;
import com.soonmin.final_project.domain.dto.PostDto;
import com.soonmin.final_project.domain.dto.PostUpdateRequest;
import com.soonmin.final_project.domain.dto.PostViewResponse;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    public PostDto create(PostCreateRequest request, Authentication authentication) {
        User user = userRepository.findByUserName(authentication.getName())
                .orElseThrow(() -> new LikeLionException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));
        Post post = postRepository.save(request.toEntity(user));
        return post.toDto();
    }

    public PostDto viewDetail(Integer id) {
        Optional<Post> post = postRepository.findById(id);
        return post.get().toDto();
    }

    @Transactional
    public List<PostViewResponse> viewList(Pageable pageable) {
        Page<Post> postList = postRepository.findAll(pageable);
        return postList.stream().map(post -> post.toDto().toViewResponse()).collect(Collectors.toList());
    }

    public Integer delete(Integer id, Authentication authentication) {
        Post post = postRepository.findById(id)
                .orElseThrow(()->new LikeLionException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        log.info("userName get at post : {}", post.getUser().getUserName());
        log.info("userName get at authentication : {}", authentication.getName());
        if (!post.getUser().getUserName().equals(authentication.getName())){
            throw new LikeLionException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
        }

        postRepository.deleteById(id);

        return id;
    }

    public Integer update(Integer id, Authentication authentication, PostUpdateRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(()->new LikeLionException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        if (!post.getUser().getUserName().equals(authentication.getName())){
            throw new LikeLionException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
        }

        post.update(request.getTitle(), request.getBody());
        postRepository.save(post);

        return id;
    }
}
