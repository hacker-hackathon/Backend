package com.hacker.hackathon.service;

import com.hacker.hackathon.common.response.ApiResponse;
import com.hacker.hackathon.common.response.ErrorMessage;
import com.hacker.hackathon.common.response.SuccessMessage;
import com.hacker.hackathon.dto.TodoListDTO;
import com.hacker.hackathon.dto.TodoListUpdateDTO;
import com.hacker.hackathon.model.UserTodoList;
import com.hacker.hackathon.model.UserTodoQuiz;
import com.hacker.hackathon.model.UserTodoVideo;
import com.hacker.hackathon.repository.UserTodoListRepository;
import com.hacker.hackathon.repository.UserTodoQuizRepository;
import com.hacker.hackathon.repository.UserTodoVideoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodoListService {
    private final UserTodoListRepository userTodoListRepository;
    private final UserTodoVideoRepository userTodoVideoRepository;
    private final UserTodoQuizRepository userTodoQuizRepository;

    public TodoListService(UserTodoListRepository userTodoListRepository,
                           UserTodoVideoRepository userTodoVideoRepository, UserTodoQuizRepository userTodoQuizRepository) {
        this.userTodoListRepository = userTodoListRepository;
        this.userTodoVideoRepository = userTodoVideoRepository;
        this.userTodoQuizRepository = userTodoQuizRepository;
    }

    public ApiResponse<List<TodoListDTO>> getTodoListByUser(Long userId){
        List<UserTodoList> userTodoLists = userTodoListRepository.findByUsers_UsersId(userId);
        List<TodoListDTO> userTodoListDTOs = userTodoLists.stream()
                .map(userTodoList -> new TodoListDTO(userTodoList.getUserTodoListId(), userTodoList.getName(), userTodoList.getDescription()))
                .collect(Collectors.toList());
        return ApiResponse.success(SuccessMessage.USER_TODO_LIST_GET_SUCCESS, userTodoListDTOs);
    }

    public ApiResponse<UserTodoList> getTodoList(Long userId, Long listId){
        UserTodoList userTodoList = userTodoListRepository.findById(listId).get();
        return ApiResponse.success(SuccessMessage.USER_TODO_LIST_GET_SUCCESS, userTodoList);
    }

    public ApiResponse<UserTodoList> updateTodoList(Long userId, Long listId, TodoListUpdateDTO todoListUpdateDTO){
        if(!userTodoListRepository.existsById(listId)){
            return ApiResponse.error(ErrorMessage.NOT_FOUND_LIST_EXCEPTION, "리스트가 존재하지 않습니다");
        }

        if(todoListUpdateDTO.getQuizVideo()==Boolean.TRUE){
            if(!userTodoQuizRepository.existsById(todoListUpdateDTO.getId())){
                return ApiResponse.error(ErrorMessage.NOT_FOUND_QUIZ_EXCEPTION, "퀴즈가 존재하지 않습니다.");
            }
            UserTodoQuiz userTodoQuiz = userTodoQuizRepository.findById(todoListUpdateDTO.getId()).get();
            userTodoQuiz.setStage(todoListUpdateDTO.getArrival());
            userTodoQuizRepository.save(userTodoQuiz);
        }
        else{
            if(!userTodoVideoRepository.existsById(todoListUpdateDTO.getId())){
                return ApiResponse.error(ErrorMessage.VIDEO_NOT_FOUND, "퀴즈가 존재하지 않습니다.");
            }
            UserTodoVideo userTodoVideo = userTodoVideoRepository.findById(todoListUpdateDTO.getId()).get();
            userTodoVideo.setStage(todoListUpdateDTO.getArrival());
            userTodoVideoRepository.save(userTodoVideo);
        }
        return ApiResponse.success(SuccessMessage.USER_TODO_LIST_UPDATE_SUCCESS, userTodoListRepository.findById(listId).get());
    }
}
