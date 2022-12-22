package com.soonmin.final_project.configuration;

import com.soonmin.final_project.domain.entity.User;
import com.soonmin.final_project.exception.ErrorCode;
import com.soonmin.final_project.exception.LikeLionException;
import com.soonmin.final_project.service.UserService;
import com.soonmin.final_project.utils.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            log.info("헤더 없음 혹은 잘못된 형태");
            log.info("헤더 : {}", authorizationHeader);
            filterChain.doFilter(request, response);
            return;
        }

        String token;

        try {
            token = authorizationHeader.split(" ")[1];
        } catch (Exception e) {
            log.info("token 추출 실패");
            filterChain.doFilter(request, response);
            return;
        }

        log.info("token 추출 성공, token = {}",token);
        log.info("secretKey : {}", secretKey);

        if (JwtUtil.isExpired(token, secretKey)) {
            log.info("토큰 만료");
            filterChain.doFilter(request, response);
            return;
        }

        String userName = JwtUtil.getUserName(token, secretKey);
        log.info("userName : {}", userName);

        User user = userService.getUserByUserName(userName);
        log.info("userRole : {}", user.getRole());

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), null, List.of(new SimpleGrantedAuthority(user.getRole().toString())));
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
