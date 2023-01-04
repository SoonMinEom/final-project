package com.soonmin.final_project.repository;

import com.soonmin.final_project.domain.entity.Post;
import com.soonmin.final_project.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
    Page<Post> findByUser(User user, Pageable pageable);
}
