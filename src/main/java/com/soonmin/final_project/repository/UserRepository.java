package com.soonmin.final_project.repository;

import com.soonmin.final_project.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
