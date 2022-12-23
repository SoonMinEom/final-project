package com.soonmin.final_project.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soonmin.final_project.domain.ErrorResponse;
import com.soonmin.final_project.domain.Response;
import com.soonmin.final_project.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INVALID_PERMISSION.name(), ErrorCode.INVALID_PERMISSION.getMessage());

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error("UnAuthorized : {}", authException.getMessage());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(ErrorCode.INVALID_PERMISSION.getStatus().value());

        try (OutputStream os = response.getOutputStream()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(os, Response.error(errorResponse));
            os.flush();
        }
    }

}
