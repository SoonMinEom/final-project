package com.soonmin.final_project.controller;

import com.soonmin.final_project.service.AlgorithmService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/v1/hello")
@RequiredArgsConstructor
@ApiIgnore
public class HelloRestController {

    private final AlgorithmService algorithmService;

    @GetMapping
    public String helloTest() {
        return "엄순민";
    }

    @GetMapping("/{num}")
    public Integer sumOfDigit(@PathVariable Integer num) {
        Integer answer = algorithmService.sumOfDigit(num);
        return answer;
    }
}
