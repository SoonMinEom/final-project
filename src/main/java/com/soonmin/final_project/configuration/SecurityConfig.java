package com.soonmin.final_project.configuration;

import com.soonmin.final_project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;

    @Value("${jwt.token.secret}")
    private String secretKey;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic().disable()
                .csrf().disable()
                .cors().and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/v1/users/{id}/role/change", "/api/v1/posts",  "/api/v1/posts/{postId}/comments", "api/v1/posts/{postId}/likes").authenticated()
                .antMatchers(HttpMethod.GET, "/api/v1/posts/my").authenticated()
                .antMatchers(HttpMethod.PUT,"/api/v1/posts/{id}", "/api/v1/posts/{postId}/comments/{id}").authenticated()
                .antMatchers(HttpMethod.DELETE, "/api/v1/posts/{id}", "/api/v1/posts/{postId}/comments/{id}").authenticated()
                .anyRequest().permitAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                .addFilterBefore(new JwtTokenFilter(userService, secretKey), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
