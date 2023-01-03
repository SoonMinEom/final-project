package com.soonmin.final_project.domain.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommentDeleteResponse {
    private String message;
    private Integer id;
}
