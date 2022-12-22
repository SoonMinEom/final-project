package com.soonmin.final_project.repository;

import com.soonmin.final_project.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
}
