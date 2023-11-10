package com.hacker.hackathon.controller;

import com.hacker.hackathon.common.response.ApiResponse;
import com.hacker.hackathon.dto.*;
import com.hacker.hackathon.model.UserTodoList;
import com.hacker.hackathon.service.UserTodoListService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserTodoListController {

    private final UserTodoListService userTodoListService;

    public UserTodoListController(UserTodoListService userTodoListService) {
        this.userTodoListService = userTodoListService;
    }

    @GetMapping("/user/{userId}/list")
    public ApiResponse<List<UserTodoListDTO>> getTodoListAll(@PathVariable Long userId){
        return userTodoListService.getTodoListByUser(userId);
    }

    @GetMapping("/user/{userId}/list/{listId}")
    public ApiResponse<UserTodoListByIdDTO> getTodoList(@PathVariable Long userId, @PathVariable Long listId){
        return userTodoListService.getTodoList(userId, listId);
    }

    @PutMapping("/list/{listId}")
    public ApiResponse<UserTodoList> updateTodoList(@PathVariable Long listId, @RequestBody TodoListUpdateDTO todoListUpdateDTO){
        return userTodoListService.updateTodoList(listId, todoListUpdateDTO);
    }

    @DeleteMapping("/user/{userId}/list/{listId}")
    public ApiResponse<TodoListDeleteDTO> deleteTodoList(@PathVariable Long userId, @PathVariable Long listId){
        return userTodoListService.deleteTodoList(userId, listId);
    }

    @PostMapping("/user/{userId}/list/{listId}")
    public ApiResponse<NewUserTodoListDTO> newTodoList(@PathVariable Long userId, @PathVariable Long listId){
        return userTodoListService.newTodoList(userId, listId);
    }

    @GetMapping("/list/{listId}/{stage}")
    public ApiResponse<ListByStageDTO> getListByStage(@PathVariable Long listId, @PathVariable Long stage){
        return userTodoListService.getListByStage(listId, stage);
    }
}
