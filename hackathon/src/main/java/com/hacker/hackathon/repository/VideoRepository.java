package com.hacker.hackathon.repository;

import com.hacker.hackathon.model.Quiz;
import com.hacker.hackathon.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface VideoRepository extends JpaRepository<Video,Long> {

    boolean existsByLink(String link);
}
