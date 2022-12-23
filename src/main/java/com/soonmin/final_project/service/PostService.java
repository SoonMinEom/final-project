package com.soonmin.final_project.service;

import com.soonmin.final_project.domain.dto.PostCreateRequest;
import com.soonmin.final_project.domain.dto.PostDto;
import com.soonmin.final_project.domain.dto.PostViewResponse;
import com.soonmin.final_project.domain.entity.Post;
import com.soonmin.final_project.domain.entity.User;
import com.soonmin.final_project.exception.ErrorCode;
import com.soonmin.final_project.exception.LikeLionException;
import com.soonmin.final_project.repository.PostRepository;
import com.soonmin.final_project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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
}
