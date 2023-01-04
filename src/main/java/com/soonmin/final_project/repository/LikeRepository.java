package com.soonmin.final_project.repository;

import com.soonmin.final_project.domain.entity.Like;
import com.soonmin.final_project.domain.entity.Post;
import com.soonmin.final_project.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Integer> {
    Optional<Like> findByPostAndUser(Post post, User user);
    List<Like> findAllByPost(Post post);
}
