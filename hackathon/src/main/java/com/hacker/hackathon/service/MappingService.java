package com.hacker.hackathon.service;

import com.hacker.hackathon.common.response.ApiResponse;
import com.hacker.hackathon.common.response.ErrorMessage;
import com.hacker.hackathon.common.response.SuccessMessage;
import com.hacker.hackathon.dto.ListQuizDTO;
import com.hacker.hackathon.dto.ListVideoDTO;
import com.hacker.hackathon.model.*;
import com.hacker.hackathon.repository.*;
import org.springframework.stereotype.Service;

@Service
public class MappingService {
    private final TodoListRepository todoListRepository;
    private final QuizRepository quizRepository;
    private final VideoRepository videoRepository;
    private final TodoQuizRepository todoQuizRepository;
    private final TodoVideoRepository todoVideoRepository;


    public MappingService(TodoListRepository todoListRepository, QuizRepository quizRepository, VideoRepository videoRepository, TodoQuizRepository todoQuizRepository, TodoVideoRepository todoVideoRepository) {
        this.todoListRepository = todoListRepository;
        this.quizRepository = quizRepository;
        this.videoRepository = videoRepository;
        this.todoQuizRepository = todoQuizRepository;
        this.todoVideoRepository = todoVideoRepository;
    }

    public ApiResponse<ListVideoDTO> mapVideoToList(Long listId, Long videoId){
        if(!todoListRepository.existsById(listId)){
            return ApiResponse.error(ErrorMessage.NOT_FOUND_LIST_EXCEPTION);
        }
        if(!videoRepository.existsById(videoId)){
            return ApiResponse.error(ErrorMessage.VIDEO_NOT_FOUND);
        }
        TodoList todoList = todoListRepository.findById(listId).get();
        Video video = videoRepository.findById(videoId).get();
        for (Quiz quiz : video.getQuizzes()) {
            Long quizId = quiz.getQuizId();
            mapQuizToList(listId, quizId);
        }
        TodoVideo todoVideo = new TodoVideo();
        todoVideo.setVideo(video);
        todoVideo.setTodoList(todoList);
        todoVideoRepository.save(todoVideo);
        ListVideoDTO listVideoDTO = new ListVideoDTO();
        listVideoDTO.setListId(listId);
        listVideoDTO.setVideoId(videoId);
        return ApiResponse.success(SuccessMessage.CREATE_VIDEO_SUCCESS, listVideoDTO);
    }

    public ApiResponse<ListQuizDTO> mapQuizToList(Long listId, Long quizId){
        if(!todoListRepository.existsById(listId)){
            return ApiResponse.error(ErrorMessage.NOT_FOUND_LIST_EXCEPTION);
        }
        if(!quizRepository.existsById(quizId)){
            return ApiResponse.error(ErrorMessage.NOT_FOUND_QUIZ_EXCEPTION);
        }
        TodoList todoList = todoListRepository.findById(listId).get();
        Quiz quiz = quizRepository.findById(quizId).get();
        TodoQuiz todoQuiz = new TodoQuiz();
        todoQuiz.setQuiz(quiz);
        todoQuiz.setTodoList(todoList);
        todoQuizRepository.save(todoQuiz);
        ListQuizDTO listQuizDTO = new ListQuizDTO();
        listQuizDTO.setListId(listId);
        listQuizDTO.setQuizId(quizId);
        return ApiResponse.success(SuccessMessage.GET_QUIZ_SUCCESS, listQuizDTO);
    }
}
