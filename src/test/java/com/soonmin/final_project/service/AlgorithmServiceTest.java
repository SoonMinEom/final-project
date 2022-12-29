package com.soonmin.final_project.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlgorithmServiceTest {

    AlgorithmService algorithmService = new AlgorithmService();

    @Test
    @DisplayName("자릿수의 합 구하기")
    void sumOfDigit() {
        assertEquals(6, algorithmService.sumOfDigit(123));
        assertEquals(17, algorithmService.sumOfDigit(4256));
        assertEquals(5, algorithmService.sumOfDigit(23));
    }
}