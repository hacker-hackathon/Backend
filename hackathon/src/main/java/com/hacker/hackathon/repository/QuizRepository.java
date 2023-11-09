package com.hacker.hackathon.repository;

import com.hacker.hackathon.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface QuizRepository extends JpaRepository<Quiz,Long> {
    List<Quiz> findByVideo_VideoId(Long videoId);
}
