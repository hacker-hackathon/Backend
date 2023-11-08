package com.hacker.hackathon.repository;

import com.hacker.hackathon.model.UserTodoVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface UserTodoVideoRepository extends JpaRepository<UserTodoVideo,Long> {
}
