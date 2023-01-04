package com.soonmin.final_project.service;

import com.soonmin.final_project.domain.dto.alarm.AlarmType;
import com.soonmin.final_project.domain.entity.Alarm;
import com.soonmin.final_project.domain.entity.Like;
import com.soonmin.final_project.domain.entity.Post;
import com.soonmin.final_project.domain.entity.User;
import com.soonmin.final_project.exception.ErrorCode;
import com.soonmin.final_project.exception.LikeLionException;
import com.soonmin.final_project.repository.AlarmRepository;
import com.soonmin.final_project.repository.LikeRepository;
import com.soonmin.final_project.repository.PostRepository;
import com.soonmin.final_project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final AlarmRepository alarmRepository;


    // 좋아요 누르기
    public Integer like(Integer postId, String userName) {
        // 해당 포스트가 있는지 검증
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new LikeLionException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        // 유저가 존재하는지 검증(로그인)
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new LikeLionException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));

        // 좋아요 누르기
        // 해당 포스트에 해당 유저의 좋아요가 이미 있는지 검증
        Optional<Like> optionalLike = likeRepository.findByPostAndUser(post, user);
        if (optionalLike.isPresent()) {
            // 존재하면 지우고
            likeRepository.delete(optionalLike.get());
            return 0;
        } else {
            // 존재하지 않으면 추가하고
            likeRepository.save(Like.pushLike(post, user));
            // 알람 보내기
            alarmRepository.save(Alarm.makeAlarm(AlarmType.NEW_LIKE_ON_POST, post.getUser(), user.getId(), post.getId()));
            return 1;
        }
    }

    // 좋아요 개수
    public Integer numOfLikes(Integer postId) {
        // 해당 포스트가 있는지 검증
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new LikeLionException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        // 해당 포스트의 좋아요 검색
        List<Like> numOfLikes = likeRepository.findAllByPost(post);

        return numOfLikes.size();
    }
}
