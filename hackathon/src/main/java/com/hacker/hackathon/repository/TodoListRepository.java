package com.hacker.hackathon.repository;

import com.hacker.hackathon.model.TodoList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface TodoListRepository extends JpaRepository<TodoList,Long> {
}
