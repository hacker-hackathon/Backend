package com.hacker.hackathon.repository;

import com.hacker.hackathon.model.UserVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface UserVideoRepository extends JpaRepository<UserVideo,Long> {
    boolean existsByVideo_VideoIdAndUsers_UsersId(Long videoId, Long usersId);
}
