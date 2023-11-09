package com.hacker.hackathon.controller;

import com.hacker.hackathon.common.response.ApiResponse;
import com.hacker.hackathon.dto.TodoListDTO;
import com.hacker.hackathon.dto.TodoListUpdateDTO;
import com.hacker.hackathon.model.UserTodoList;
import com.hacker.hackathon.service.TodoListService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TodoListController {

    private final TodoListService todoListService;

    public TodoListController(TodoListService todoListService) {
        this.todoListService = todoListService;
    }

    @GetMapping("/user/{userId}/list")
    public ApiResponse<List<TodoListDTO>> getTodoListAll(@PathVariable Long userId){
        return todoListService.getTodoListByUser(userId);
    }

    @GetMapping("/user/{userId}/list/{listId}")
    public ApiResponse<UserTodoList> getTodoList(@PathVariable Long userId, @PathVariable Long listId){
        return todoListService.getTodoList(userId, listId);
    }

    @PutMapping("/user/{userId}/list/{listId}")
    public ApiResponse<UserTodoList> updateTodoList(@PathVariable Long userId, @PathVariable Long listId, @ModelAttribute TodoListUpdateDTO todoListUpdateDTO){
        return todoListService.updateTodoList(userId, listId, todoListUpdateDTO);
    }


}
