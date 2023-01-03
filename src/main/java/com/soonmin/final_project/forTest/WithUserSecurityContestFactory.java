package com.soonmin.final_project.forTest;

import com.soonmin.final_project.domain.dto.user.UserJoinRequest;
import com.soonmin.final_project.domain.entity.User;
import com.soonmin.final_project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

@RequiredArgsConstructor
public class WithUserSecurityContestFactory implements WithSecurityContextFactory<WithUser> {

    private final UserService userService;
    @Override
    public SecurityContext createSecurityContext(WithUser withUser) {
        String username = withUser.value();

        UserJoinRequest request = new UserJoinRequest(username, "1234");
        userService.join(request);

        User principal = userService.getUserByUserName(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                principal,
                principal.getPassword());
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);

        return securityContext;
    }
}
