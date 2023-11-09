package com.hacker.hackathon.service;

import com.hacker.hackathon.common.response.ApiResponse;
import com.hacker.hackathon.common.response.ErrorMessage;
import com.hacker.hackathon.common.response.SuccessMessage;
import com.hacker.hackathon.dto.*;
import com.hacker.hackathon.model.UserTodoList;
import com.hacker.hackathon.model.UserTodoQuiz;
import com.hacker.hackathon.model.UserTodoVideo;
import com.hacker.hackathon.repository.UserTodoListRepository;
import com.hacker.hackathon.repository.UserTodoQuizRepository;
import com.hacker.hackathon.repository.UserTodoVideoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserTodoListService {
    private final UserTodoListRepository userTodoListRepository;
    private final UserTodoVideoRepository userTodoVideoRepository;
    private final UserTodoQuizRepository userTodoQuizRepository;

    public UserTodoListService(UserTodoListRepository userTodoListRepository,
                               UserTodoVideoRepository userTodoVideoRepository, UserTodoQuizRepository userTodoQuizRepository) {
        this.userTodoListRepository = userTodoListRepository;
        this.userTodoVideoRepository = userTodoVideoRepository;
        this.userTodoQuizRepository = userTodoQuizRepository;
    }

    public ApiResponse<List<UserTodoListDTO>> getTodoListByUser(Long userId){
        List<UserTodoList> userTodoLists = userTodoListRepository.findByUsers_UsersId(userId);
        List<UserTodoListDTO> userUserTodoListDTOS = userTodoLists.stream()
                .map(userTodoList -> new UserTodoListDTO(userTodoList.getUserTodoListId(), userTodoList.getName(), userTodoList.getDescription()))
                .collect(Collectors.toList());
        return ApiResponse.success(SuccessMessage.USER_TODO_LIST_GET_SUCCESS, userUserTodoListDTOS);
    }

    public ApiResponse<UserTodoListByIdDTO> getTodoList(Long userId, Long listId) {
        Optional<UserTodoList> userTodoListOpt = userTodoListRepository.findById(listId);

        if (!userTodoListOpt.isPresent()) {
            return ApiResponse.error(ErrorMessage.NOT_FOUND_LIST_EXCEPTION, null);
        }

        UserTodoList userTodoList = userTodoListOpt.get();
        UserTodoListByIdDTO dto = new UserTodoListByIdDTO();
        dto.setUserTodoListId(userTodoList.getUserTodoListId());
        dto.setName(userTodoList.getName());
        dto.setDescription(userTodoList.getDescription());

        List<UserTodoVideoDTO> userTodoVideoDTOs = userTodoList.getUserTodoVideos().stream()
                .map(utv -> {
                    UserTodoVideoDTO videoDTO = new UserTodoVideoDTO();
                    videoDTO.setUserTodoVideoId(utv.getUserTodoVideoId());
                    videoDTO.setStage(utv.getStage());
                    videoDTO.setCompleteAt(utv.getCompleteAt());
                    videoDTO.setCompletedAt(utv.getCompletedAt());
                    videoDTO.setVideoId(utv.getVideo().getVideoId());
                    return videoDTO;
                }).collect(Collectors.toList());
        dto.setUserTodoVideos(userTodoVideoDTOs);

        List<UserTodoQuizDTO> userTodoQuizDTOs = userTodoList.getUserTodoQuizzes().stream()
                .map(utq -> {
                    UserTodoQuizDTO quizDTO = new UserTodoQuizDTO();
                    quizDTO.setUserTodoQuizId(utq.getUserTodoQuizId());
                    quizDTO.setStage(utq.getStage());
                    quizDTO.setPreviousAnswer(utq.getPreviousAnswer());
                    quizDTO.setCompleteAt(utq.getCompleteAt());
                    quizDTO.setCompletedAt(utq.getCompletedAt());
                    quizDTO.setQuizId(utq.getQuiz().getQuizId());
                    return quizDTO;
                }).collect(Collectors.toList());
        dto.setUserTodoQuizzes(userTodoQuizDTOs);

        return ApiResponse.success(SuccessMessage.USER_TODO_LIST_GET_SUCCESS, dto);
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

    public ApiResponse<TodoListDeleteDTO> deleteTodoList(Long userId, Long listId){
        if(!userTodoListRepository.existsById(listId)){
            return ApiResponse.error(ErrorMessage.NOT_FOUND_LIST_EXCEPTION, "리스트가 존재하지 않습니다");
        }

        userTodoListRepository.deleteById(listId);
        TodoListDeleteDTO todoListDeleteDTO = new TodoListDeleteDTO("리스트 삭제에 성공했습니다.");
        return ApiResponse.success(SuccessMessage.TODO_LIST_DELETE_SUCCESS,todoListDeleteDTO);
    }
}
