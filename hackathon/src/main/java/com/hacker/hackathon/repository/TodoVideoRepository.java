package com.hacker.hackathon.repository;

import com.hacker.hackathon.model.TodoVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface TodoVideoRepository extends JpaRepository<TodoVideo,Long> {
}
