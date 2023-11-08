package com.hacker.hackathon.repository;

import com.hacker.hackathon.model.TodoQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface TodoQuizRepository extends JpaRepository<TodoQuiz,Long> {
}
