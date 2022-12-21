package com.soonmin.final_project.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/hello")
public class HelloController {

    @GetMapping
    public String helloTest() {
        return "hello";
    }


    @GetMapping("/2")
    public String helloTest2() {
        return "test";
    }
}
