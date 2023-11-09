package com.hacker.hackathon.repository;

import com.hacker.hackathon.model.UserTodoList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface UserTodoListRepository extends JpaRepository<UserTodoList,Long> {
    List<UserTodoList> findByUsers_UsersId(Long userId);
}
