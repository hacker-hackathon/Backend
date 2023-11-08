package com.hacker.hackathon.repository;

import com.hacker.hackathon.model.UserTodoList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface UserTodoListRepository extends JpaRepository<UserTodoList,Long> {
}
