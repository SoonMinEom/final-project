package com.soonmin.final_project.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LikeLionException extends RuntimeException {

    private ErrorCode errorCode;
    private String message;
}
