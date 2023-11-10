package com.hacker.hackathon.service;

import com.hacker.hackathon.common.response.ApiResponse;
import com.hacker.hackathon.common.response.ErrorMessage;
import com.hacker.hackathon.common.response.SuccessMessage;
import com.hacker.hackathon.dto.*;
import com.hacker.hackathon.model.Quiz;
import com.hacker.hackathon.model.UserTodoQuiz;
import com.hacker.hackathon.model.Video;
import com.hacker.hackathon.repository.QuizRepository;
import com.hacker.hackathon.repository.UserTodoQuizRepository;
import com.hacker.hackathon.repository.VideoRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuizService {
    private final VideoRepository videoRepository;
    Logger logger = LoggerFactory.getLogger(QuizService.class);
    private final UserTodoQuizRepository userTodoQuizRepository;
    private final QuizRepository quizRepository;
    public QuizService(VideoRepository videoRepository, UserTodoQuizRepository userTodoQuizRepository, QuizRepository quizRepository) {
        this.videoRepository = videoRepository;
        this.userTodoQuizRepository = userTodoQuizRepository;
        this.quizRepository = quizRepository;
    }

    public ApiResponse<VideoQuizDTO> getQuizByVideoId(Long videoId){
        if (!videoRepository.existsById(videoId)) {
            return ApiResponse.error(ErrorMessage.VIDEO_NOT_FOUND, "No video found with the given ID.");
        }

        Video video = videoRepository.findById(videoId).orElse(null);

        if (video == null || video.getQuizzes().isEmpty()) {
            return ApiResponse.error(ErrorMessage.NOT_FOUND_QUIZ_EXCEPTION, "No quizzes found for the given video ID.");
        }

        List<QuizDTO> quizDTOList = video.getQuizzes().stream()
                .map(quiz -> new QuizDTO(quiz.getQuizId(), quiz.getQuestion(), quiz.getAnswer(), quiz.getS3Url()))
                .collect(Collectors.toList());

        VideoQuizDTO videoQuizDTO = new VideoQuizDTO(video.getVideoId(), quizDTOList);

        return ApiResponse.success(SuccessMessage.GET_QUIZ_SUCCESS, videoQuizDTO);

    }

    public ApiResponse<QuizDTO> getQuizById(Long quizId){
        Quiz quiz = quizRepository.findById(quizId).orElse(null);

        if (quiz == null) {
            return ApiResponse.error(ErrorMessage.NOT_FOUND_QUIZ_EXCEPTION, "No quiz found with the given ID.");
        }

        QuizDTO quizDTO = new QuizDTO(quiz.getQuizId(), quiz.getQuestion(), quiz.getAnswer(), quiz.getS3Url());

        return ApiResponse.success(SuccessMessage.GET_QUIZ_SUCCESS, quizDTO);

    }

    public ApiResponse<UserQuizDTO> getQuizByUserIdAndVideoId(Long userId, Long videoId) {
        List<Quiz> quizzes = quizRepository.findByVideo_VideoId(videoId);

        List<UserTodoQuizDTO> userTodoQuizDTOs = quizzes.stream()
                .map(quiz -> userTodoQuizRepository.findByQuiz_QuizIdAndUsers_UsersId(quiz.getQuizId(), userId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(this::convertToUserTodoQuizDTO)
                .collect(Collectors.toList());

        UserQuizDTO userQuizDTO = new UserQuizDTO(userId, userTodoQuizDTOs);

        return ApiResponse.success(SuccessMessage.GET_QUIZ_SUCCESS, userQuizDTO);
    }

    private UserTodoQuizDTO convertToUserTodoQuizDTO(UserTodoQuiz userTodoQuiz) {
        UserTodoQuizDTO dto = new UserTodoQuizDTO();
        dto.setUserTodoQuizId(userTodoQuiz.getUserTodoQuizId());
        dto.setStage(userTodoQuiz.getStage());
        dto.setPreviousAnswer(userTodoQuiz.getPreviousAnswer());
        dto.setCompleteAt(userTodoQuiz.getCompleteAt());
        dto.setQuizId(userTodoQuiz.getQuiz().getQuizId());
        dto.setQuestion(userTodoQuiz.getQuiz().getQuestion());
        dto.setS3Url(userTodoQuiz.getQuiz().getS3Url());

        return dto;
    }

    public ApiResponse<UserTodoQuizDTO> getQuizByUserIdAndQuizId(Long userId, Long quizId){
        Optional<UserTodoQuiz> userTodoQuizOptional = userTodoQuizRepository.findById(quizId);

        if (userTodoQuizOptional.isPresent()) {
            UserTodoQuiz userTodoQuiz = userTodoQuizOptional.get();
            UserTodoQuizDTO userTodoQuizDTO = convertToUserTodoQuizDTO(userTodoQuiz);
            return ApiResponse.success(SuccessMessage.GET_QUIZ_SUCCESS, userTodoQuizDTO);
        } else {
            return ApiResponse.error(ErrorMessage.NOT_FOUND_QUIZ_EXCEPTION, "Quiz not found for the given user and quiz IDs.");
        }
    }

    @Transactional
    public ApiResponse<QuizMessageDTO> solveQuiz(Long userId, Long quizId, Long answer) {

        Optional<Quiz> quizOptional = quizRepository.findById(quizId);
        if (!quizOptional.isPresent()) {
            return ApiResponse.error(ErrorMessage.NOT_FOUND_QUIZ_EXCEPTION,"Quiz not found.");
        }
        Quiz quiz = quizOptional.get();

        Boolean isCorrect = (answer == 1 && quiz.getAnswer()) || (answer == 2 && !quiz.getAnswer());

        Optional<UserTodoQuiz> userTodoQuizOptional = userTodoQuizRepository.findByQuiz_QuizIdAndUsers_UsersId(quizId, userId);
        if (!userTodoQuizOptional.isPresent()) {
            return ApiResponse.error(ErrorMessage.NOT_FOUND_QUIZ_EXCEPTION, "UserTodoQuiz not found.");
        }
        UserTodoQuiz userTodoQuiz = userTodoQuizOptional.get();

        userTodoQuiz.setStage(isCorrect ? 4L : 2L);
        userTodoQuiz.setPreviousAnswer(isCorrect);

        userTodoQuizRepository.save(userTodoQuiz);
        logger.info(String.valueOf(userTodoQuiz.getCompletedAt()));

        QuizMessageDTO quizMessageDTO = new QuizMessageDTO();
        quizMessageDTO.setMessage(isCorrect ? Boolean.TRUE : Boolean.FALSE);
        quizMessageDTO.setQuizId(quizId);
        quizMessageDTO.setUsersId(userId);

        return ApiResponse.success(SuccessMessage.QUIZ_SOLVE_SUCCESS, quizMessageDTO);
    }

}
