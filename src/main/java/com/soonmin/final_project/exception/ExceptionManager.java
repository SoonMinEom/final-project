package com.soonmin.final_project.exception;

import com.soonmin.final_project.domain.ErrorResponse;
import com.soonmin.final_project.domain.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionManager {

    @ExceptionHandler(LikeLionException.class)
    public ResponseEntity<?> LikeLionExceptionHandler (LikeLionException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode().name(), e.getMessage());
        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(Response.error(errorResponse));
    }
}
