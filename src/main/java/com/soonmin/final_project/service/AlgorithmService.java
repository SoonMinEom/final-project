package com.soonmin.final_project.service;

import org.springframework.stereotype.Service;

@Service
public class AlgorithmService {

    public Integer sumOfDigit(Integer num) {
        Integer answer = 0;

        while (num >0) {
            answer += num % 10;
            num = num / 10;
        }

        return answer;
    }
}
