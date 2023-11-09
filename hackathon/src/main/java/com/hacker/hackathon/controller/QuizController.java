package com.hacker.hackathon.controller;

import com.hacker.hackathon.common.response.ApiResponse;
import com.hacker.hackathon.dto.*;
import com.hacker.hackathon.service.QuizService;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuizController {
    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("/video/{videoId}/quiz")
    public ApiResponse<VideoQuizDTO> getQuizByVideoId(@PathVariable Long videoId){
        return quizService.getQuizByVideoId(videoId);
    }

    @GetMapping("/quiz/{quizId}")
    public ApiResponse<QuizDTO> getQuizById(@PathVariable Long quizId){
        return quizService.getQuizById(quizId);
    }

    @GetMapping("/user/{userId}/video/{videoId}/quiz")
    public ApiResponse<UserQuizDTO> getQuizByUserIdAndVideoId(@PathVariable Long userId, @PathVariable Long videoId){
        return quizService.getQuizByUserIdAndVideoId(userId, videoId);
    }

    @GetMapping("/user/{userId}/quiz/{quizId}")
    public ApiResponse<UserTodoQuizDTO> getQuizByUserIdAndQuizId(@PathVariable Long userId, @PathVariable Long quizId){
        return quizService.getQuizByUserIdAndQuizId(userId, quizId);
    }

    @PostMapping("/user/{userId}/quiz/{quizId}/{answer}")
    public ApiResponse<QuizMessageDTO> solveQuiz(@PathVariable Long userId, @PathVariable Long quizId, @PathVariable Long answer){
        return quizService.solveQuiz(userId, quizId, answer);
    }
}
