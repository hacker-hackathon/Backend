package com.hacker.hackathon.repository;

import com.hacker.hackathon.model.UserTodoQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface UserTodoQuizRepository extends JpaRepository<UserTodoQuiz,Long> {
    boolean existsByQuiz_QuizIdAndUsers_UsersId(Long quizId, Long usersId);
}
