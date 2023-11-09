package com.hacker.hackathon.controller;

import com.hacker.hackathon.common.response.ApiResponse;
import com.hacker.hackathon.dto.TodoListAllDTO;
import com.hacker.hackathon.service.TodoListService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class TodoListController {
    private final TodoListService todoListService;

    public TodoListController(TodoListService todoListService) {
        this.todoListService = todoListService;
    }

    @GetMapping("/list")
    public ApiResponse<List<TodoListAllDTO>> getTodoList(){
        return todoListService.getTodoList();
    }

    @GetMapping("/list/{listId}")
    public ApiResponse<Optional<TodoListAllDTO>> getTodoListById(@PathVariable Long listId){
        return todoListService.getTodoListbyId(listId);
    }


}
