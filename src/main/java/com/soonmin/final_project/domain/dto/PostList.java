package com.soonmin.final_project.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@AllArgsConstructor
@Getter
public class PostList {

    private List<PostViewResponse> content;
    private Pageable pageable;
}
