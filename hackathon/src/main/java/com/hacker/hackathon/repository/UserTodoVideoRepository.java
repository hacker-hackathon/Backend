package com.hacker.hackathon.repository;

import com.hacker.hackathon.model.UserTodoVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Map;
import java.util.Optional;

@RepositoryRestResource
public interface UserTodoVideoRepository extends JpaRepository<UserTodoVideo,Long> {
    boolean existsByVideo_VideoIdAndUsers_UsersId(Long videoId, Long usersId);
    UserTodoVideo findByVideo_VideoIdAndUsers_UsersId(Long videoId, Long usersId);

    Optional<UserTodoVideo> findByVideo_VideoId(Long videoId);
}
