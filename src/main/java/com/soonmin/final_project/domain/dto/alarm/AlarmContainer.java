package com.soonmin.final_project.domain.dto.alarm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AlarmContainer {
    private List<AlarmResponse> content;
}
