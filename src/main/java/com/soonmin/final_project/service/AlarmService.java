package com.soonmin.final_project.service;

import com.soonmin.final_project.domain.dto.alarm.AlarmResponse;
import com.soonmin.final_project.domain.entity.Alarm;
import com.soonmin.final_project.domain.entity.User;
import com.soonmin.final_project.exception.ErrorCode;
import com.soonmin.final_project.exception.LikeLionException;
import com.soonmin.final_project.repository.AlarmRepository;
import com.soonmin.final_project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final UserRepository userRepository;

    public List<AlarmResponse> getAlarms(String userName, Pageable pageable) {
        // 해당 유저가 존재하는지 검증
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new LikeLionException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));

        // 해당 유저가 받은 알람을 검색
        List<Alarm> alarmList = alarmRepository.findAllByUser(user);

        return alarmList.stream().map(alarm -> alarm.toResponse()).collect(Collectors.toList());
    }
}
