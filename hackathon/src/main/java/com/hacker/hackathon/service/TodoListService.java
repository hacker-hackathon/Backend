package com.hacker.hackathon.service;

import com.hacker.hackathon.common.response.ApiResponse;
import com.hacker.hackathon.common.response.SuccessMessage;
import com.hacker.hackathon.dto.TodoListAllDTO;
import com.hacker.hackathon.model.*;
import com.hacker.hackathon.repository.TodoListRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TodoListService {
    private final TodoListRepository todoListRepository;

    public TodoListService(TodoListRepository todoListRepository) {
        this.todoListRepository = todoListRepository;
    }

    public Optional<TodoListAllDTO> getTodoListById(Long todoListId) {
        return todoListRepository.findById(todoListId).map(this::convertToTodoListAllDTO);
    }
    public ApiResponse<List<TodoListAllDTO>> getTodoList() {
        List<TodoListAllDTO> todoListAllDTOS = todoListRepository.findAll().stream()
                .map(todoList -> {
                    Set<Long> videoIds = todoList.getTodoVideos().stream()
                            .map(TodoVideo::getVideo)
                            .map(Video::getVideoId)
                            .collect(Collectors.toSet());

                    Set<Long> quizIds = todoList.getTodoQuizzes().stream()
                            .map(TodoQuiz::getQuiz)
                            .map(Quiz::getQuizId)
                            .collect(Collectors.toSet());

                    return new TodoListAllDTO(
                            todoList.getTodoListId(),
                            todoList.getName(),
                            todoList.getDescription(),
                            videoIds,
                            quizIds);
                })
                .collect(Collectors.toList());
        return ApiResponse.success(SuccessMessage.TODO_LIST_GET_SUCCESS, todoListAllDTOS);
    }


    private TodoListAllDTO convertToTodoListAllDTO(TodoList todoList) {
        TodoListAllDTO dto = new TodoListAllDTO();
        dto.setTodoListId(todoList.getTodoListId());
        dto.setName(todoList.getName());
        dto.setDescription(todoList.getDescription());

        Set<Long> videoIds = todoList.getTodoVideos().stream()
                .map(TodoVideo::getVideo)
                .map(Video::getVideoId)
                .collect(Collectors.toSet());
        dto.setTodoVideos(videoIds);

        Set<Long> quizIds = todoList.getTodoQuizzes().stream()
                .map(TodoQuiz::getQuiz)
                .map(Quiz::getQuizId)
                .collect(Collectors.toSet());
        dto.setTodoQuizzes(quizIds);

        return dto;
    }

    public ApiResponse<Optional<TodoListAllDTO>> getTodoListbyId(Long listId){
        return ApiResponse.success(SuccessMessage.TODO_LIST_GET_BY_ID_SUCCESS,todoListRepository.findById(listId).map(this::convertToTodoListAllDTO));
    }
}
