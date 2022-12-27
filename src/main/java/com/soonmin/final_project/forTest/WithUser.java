package com.soonmin.final_project.forTest;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithUserSecurityContestFactory.class)
public @interface WithUser {
    String value();
}
