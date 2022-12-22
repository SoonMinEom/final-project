package com.soonmin.final_project.service;

import com.soonmin.final_project.domain.dto.PostCreateRequest;
import com.soonmin.final_project.domain.dto.PostDto;
import com.soonmin.final_project.domain.entity.Post;
import com.soonmin.final_project.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public PostDto create(PostCreateRequest request, Authentication authentication) {
        Post post = postRepository.save(request.toEntity());
        return post.toDto();
    }
}
