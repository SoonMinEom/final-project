package com.soonmin.final_project.repository;

import com.soonmin.final_project.domain.entity.Alarm;
import com.soonmin.final_project.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Integer> {
    List<Alarm> findAllByUser(User user);
}
