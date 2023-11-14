package com.hacker.hackathon.controller;

import com.hacker.hackathon.common.response.ApiResponse;
import com.hacker.hackathon.config.resolver.UserId;
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

    @GetMapping("/user/list")
    public ApiResponse<List<UserTodoListDTO>> getTodoListAll(@UserId Long userId){
        return userTodoListService.getTodoListByUser(userId);
    }

    @GetMapping("/user/list/{listId}")
    public ApiResponse<UserTodoListByIdDTO> getTodoList(@UserId Long userId, @PathVariable Long listId){
        return userTodoListService.getTodoList(userId, listId);
    }

    @PutMapping("/list/{listId}")
    public ApiResponse<UserTodoList> updateTodoList(@UserId Long userId,@PathVariable Long listId, @ModelAttribute TodoListUpdateDTO todoListUpdateDTO){
        return userTodoListService.updateTodoList(userId, listId, todoListUpdateDTO);
    }

    @DeleteMapping("/user/list/{listId}")
    public ApiResponse<TodoListDeleteDTO> deleteTodoList(@UserId Long userId, @PathVariable Long listId){
        return userTodoListService.deleteTodoList(userId, listId);
    }

    @PostMapping("/user/list/{listId}")
    public ApiResponse<NewUserTodoListDTO> newTodoList(@UserId Long userId, @PathVariable Long listId){
        return userTodoListService.newTodoList(userId, listId);
    }

    @GetMapping("/user/list/{listId}/{stage}")
    public ApiResponse<ListByStageDTO> getListByStage(@PathVariable Long listId, @PathVariable Long stage){
        return userTodoListService.getListByStage(listId, stage);
    }
}
