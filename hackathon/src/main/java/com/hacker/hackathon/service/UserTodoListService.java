package com.hacker.hackathon.service;

import com.hacker.hackathon.common.response.ApiResponse;
import com.hacker.hackathon.common.response.ErrorMessage;
import com.hacker.hackathon.common.response.SuccessMessage;
import com.hacker.hackathon.dto.*;
import com.hacker.hackathon.model.*;
import com.hacker.hackathon.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class UserTodoListService {
    private final UserTodoListRepository userTodoListRepository;
    private final UserTodoVideoRepository userTodoVideoRepository;
    private final UserTodoQuizRepository userTodoQuizRepository;
    private final UsersRepository usersRepository;
    private final TodoListRepository todoListRepository;
    private final VideoRepository videoRepository;

    public UserTodoListService(UserTodoListRepository userTodoListRepository,
                               UserTodoVideoRepository userTodoVideoRepository, UserTodoQuizRepository userTodoQuizRepository, UsersRepository usersRepository, TodoListRepository todoListRepository, VideoRepository videoRepository) {
        this.userTodoListRepository = userTodoListRepository;
        this.userTodoVideoRepository = userTodoVideoRepository;
        this.userTodoQuizRepository = userTodoQuizRepository;
        this.usersRepository = usersRepository;
        this.todoListRepository = todoListRepository;
        this.videoRepository = videoRepository;
    }

    public ApiResponse<List<UserTodoListDTO>> getTodoListByUser(Long userId) {
        List<UserTodoList> userTodoLists = userTodoListRepository.findByUsers_UsersId(userId);
        List<UserTodoListDTO> userTodoListDTOs = userTodoLists.stream()
                .map(userTodoList -> {
                    long completedCount = userTodoList.getUserTodoVideos().stream()
                            .filter(userTodoVideo -> userTodoVideo.getStage() == 4)
                            .count();
                    double progress = (double) completedCount*100 / userTodoList.getUserTodoVideos().size();
                    long roundedProgress = Math.round(progress);
                    return new UserTodoListDTO(
                            userTodoList.getUserTodoListId(),
                            userTodoList.getName(),
                            userTodoList.getDescription(),
                            roundedProgress
                    );
                })
                .collect(Collectors.toList());
        return ApiResponse.success(SuccessMessage.USER_TODO_LIST_GET_SUCCESS, userTodoListDTOs);
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
        AtomicReference<Long> completed = new AtomicReference<>(0L);
        List<UserTodoVideoDTO> userTodoVideoDTOs = userTodoList.getUserTodoVideos().stream()
                .map(utv -> {
                    UserTodoVideoDTO videoDTO = new UserTodoVideoDTO();
                    videoDTO.setUserTodoVideoId(utv.getUserTodoVideoId());
                    videoDTO.setStage(utv.getStage());
                    if(utv.getStage()==4L){
                        completed.getAndSet(completed.get() + 1);
                    }
                    videoDTO.setCompleteAt(utv.getCompleteAt());
                    videoDTO.setCompletedAt(utv.getCompletedAt());
                    videoDTO.setVideoId(utv.getVideo().getVideoId());
                    return videoDTO;
                }).collect(Collectors.toList());
        dto.setUserTodoVideos(userTodoVideoDTOs);
        Long completedValue = completed.get();
        double result = (double) completedValue*100 / userTodoVideoDTOs.size();
        Long roundedResult = Math.round(result);
        dto.setProgress(roundedResult);
        userTodoList.setProgress(roundedResult);
        userTodoListRepository.save(userTodoList);
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

    public ApiResponse<UserTodoList> updateTodoList(Long listId, TodoListUpdateDTO todoListUpdateDTO) {
        if (!userTodoListRepository.existsById(listId)) {
            return ApiResponse.error(ErrorMessage.NOT_FOUND_LIST_EXCEPTION, "리스트가 존재하지 않습니다");
        }
        if (!videoRepository.existsByTitle(todoListUpdateDTO.getId())) {
            return ApiResponse.error(ErrorMessage.VIDEO_NOT_FOUND, "퀴즈가 존재하지 않습니다.");
        }
        Video video = videoRepository.findByTitle(todoListUpdateDTO.getId()).get();
        UserTodoVideo userTodoVideo = userTodoVideoRepository.findByVideo_VideoId(video.getVideoId()).get();
        userTodoVideo.setStage(todoListUpdateDTO.getArrival());
        userTodoVideoRepository.save(userTodoVideo);

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

    @Transactional
    public ApiResponse<NewUserTodoListDTO> newTodoList(Long userId, Long listId) {

        Optional<TodoList> defaultTodoListOpt = todoListRepository.findById(listId);
        if (!defaultTodoListOpt.isPresent()) {
            return ApiResponse.error(ErrorMessage.NOT_FOUND_LIST_EXCEPTION);
        }
        TodoList defaultTodoList = defaultTodoListOpt.get();

        Optional<Users> userOpt = usersRepository.findById(userId);
        if (!userOpt.isPresent()) {
            return ApiResponse.error(ErrorMessage.NOT_FOUND_USER_EXCEPTION);
        }
        Users user = userOpt.get();

        UserTodoList newUserTodoList = new UserTodoList();
        newUserTodoList.setUsers(user);
        newUserTodoList.setName(defaultTodoList.getName());
        newUserTodoList.setDescription(defaultTodoList.getDescription());
        newUserTodoList.setProgress(0L);
        newUserTodoList.setTodoList(defaultTodoList);
        newUserTodoList = userTodoListRepository.save(newUserTodoList);


        Set<UserTodoVideosDTO> userTodoVideosDTOs = new HashSet<>();
        Set<UserTodoQuizsDTO> userTodoQuizsDTOs = new HashSet<>();

        UserTodoList finalNewUserTodoList = newUserTodoList;
        defaultTodoList.getTodoVideos().forEach(todoVideo -> {
            UserTodoVideo userTodoVideo = new UserTodoVideo();
            userTodoVideo.setVideo(todoVideo.getVideo());
            userTodoVideo.setUserTodoList(finalNewUserTodoList);
            userTodoVideo.setUsers(user);
            userTodoVideo.setStage(1L);
            userTodoVideo = userTodoVideoRepository.save(userTodoVideo);
            userTodoVideosDTOs.add(new UserTodoVideosDTO(todoVideo.getVideo().getVideoId(), userTodoVideo.getUserTodoVideoId()));
        });

        UserTodoList finalNewUserTodoList1 = newUserTodoList;
        defaultTodoList.getTodoQuizzes().forEach(todoQuiz -> {
            UserTodoQuiz userTodoQuiz = new UserTodoQuiz();
            userTodoQuiz.setQuiz(todoQuiz.getQuiz());
            userTodoQuiz.setUserTodoList(finalNewUserTodoList1);
            userTodoQuiz.setUsers(user);
            userTodoQuiz.setStage(1L);
            userTodoQuiz = userTodoQuizRepository.save(userTodoQuiz);
            userTodoQuizsDTOs.add(new UserTodoQuizsDTO(todoQuiz.getQuiz().getQuizId(), userTodoQuiz.getUserTodoQuizId()));
        });

        NewUserTodoListDTO newUserTodoListDTO = new NewUserTodoListDTO(
                newUserTodoList.getUserTodoListId(),
                listId,
                userId,
                newUserTodoList.getName(),
                newUserTodoList.getDescription(),
                userTodoVideosDTOs,
                userTodoQuizsDTOs
        );

        return ApiResponse.success(SuccessMessage.MAKE_NEW_USER_TODO_LIST_SUCCESS, newUserTodoListDTO);
    }

    public ApiResponse<ListByStageDTO> getListByStage(Long listId, Long stageId){
        if(!userTodoListRepository.existsById(listId)){
            return ApiResponse.error(ErrorMessage.NOT_FOUND_LIST_EXCEPTION, "리스트없음");
        }
        UserTodoList userTodoList = userTodoListRepository.findById(listId).get();
        ListByStageDTO listByStageDTO = new ListByStageDTO();
        listByStageDTO.setStageId(stageId);
        listByStageDTO.setUsersId(userTodoList.getUsers().getUsersId());
        listByStageDTO.setUserTodoListName(userTodoList.getName());
        listByStageDTO.setDescription(userTodoList.getDescription());

        Set<ListByStageVideoDTO> listByStageVideoDTOs = userTodoList.getUserTodoVideos().stream()
                .filter(userTodoVideo -> userTodoVideo.getStage().equals(stageId))
                .map(userTodoVideo -> {
                    ListByStageVideoDTO dto = new ListByStageVideoDTO();
                    Video video = userTodoVideo.getVideo();
                    dto.setCreator(video.getCreator());
                    dto.setLink(video.getLink());
                    dto.setTitle(video.getTitle());
                    dto.setUserTodoVideoId(userTodoVideo.getUserTodoVideoId());
                    //I need this part
                    Set<Quiz> videoQuizzes = video.getQuizzes();
                    Set<UserTodoQuizzesDTO> userTodoQuizzesDTOs = userTodoList.getUserTodoQuizzes().stream()
                            .filter(userTodoQuiz -> videoQuizzes.contains(userTodoQuiz.getQuiz()))
                            .map(userTodoQuiz -> {
                                UserTodoQuizzesDTO quizDTO = new UserTodoQuizzesDTO();
                                quizDTO.setQuizId(userTodoQuiz.getQuiz().getQuizId());
                                quizDTO.setUserTodoQuizId(userTodoQuiz.getUserTodoQuizId());
                                quizDTO.setQuestion(userTodoQuiz.getQuiz().getQuestion());
                                quizDTO.setAnswer(userTodoQuiz.getQuiz().getAnswer());
                                quizDTO.setPreviousAnswer(userTodoQuiz.getPreviousAnswer());
                                return quizDTO;
                            })
                            .collect(Collectors.toSet());

                    dto.setUserTodoQuizzesDTOS(userTodoQuizzesDTOs);
                    return dto;
                })
                .collect(Collectors.toSet());

        listByStageDTO.setListByStageVideoDTOS(listByStageVideoDTOs);

        return ApiResponse.success(SuccessMessage.GET_VIDEOS_BY_STAGE_SUCCESS,listByStageDTO);
    }
}
