package com.hacker.hackathon.service;

import com.hacker.hackathon.common.response.ApiResponse;
import com.hacker.hackathon.common.response.ErrorMessage;
import com.hacker.hackathon.common.response.SuccessMessage;
import com.hacker.hackathon.dto.VideoViewDTO;
import com.hacker.hackathon.model.UserTodoVideo;
import com.hacker.hackathon.model.UserVideo;
import com.hacker.hackathon.model.Video;
import com.hacker.hackathon.repository.UserTodoVideoRepository;
import com.hacker.hackathon.repository.UserVideoRepository;
import com.hacker.hackathon.repository.UsersRepository;
import com.hacker.hackathon.repository.VideoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class VideoService {
    private final VideoRepository videoRepository;
    private final UsersRepository usersRepository;
    private final TranscriptService transcriptService;
    private final UserTodoVideoRepository userTodoVideoRepository;
    private final UserVideoRepository userVideoRepository;
    public VideoService(VideoRepository videoRepository, UsersRepository usersRepository, TranscriptService transcriptService, UserTodoVideoRepository userTodoVideoRepository, UserVideoRepository userVideoRepository) {
        this.videoRepository = videoRepository;
        this.usersRepository = usersRepository;
        this.transcriptService = transcriptService;
        this.userTodoVideoRepository = userTodoVideoRepository;
        this.userVideoRepository = userVideoRepository;
    }

    public ApiResponse<Video> uploadVideo(String link) {
        if(videoRepository.existsByLink(link)){
            return ApiResponse.error(ErrorMessage.VIDEO_ALREADY_EXISTS, "video");
        }
        try {
            Video video = transcriptService.fetchTranscripts(link);
            return ApiResponse.success(SuccessMessage.CREATE_VIDEO_SUCCESS, video);
        } catch (EntityNotFoundException e) {
            return ApiResponse.error(ErrorMessage.NOT_FOUND_USER_EXCEPTION, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error(ErrorMessage.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public ApiResponse<Video> getVideo(Long videoId){
        if(!videoRepository.existsById(videoId)){
            return ApiResponse.error(ErrorMessage.VIDEO_NOT_FOUND, "영상이 존재하지 않습니다");
        }
        try{
            Video video = videoRepository.findById(videoId).get();
            return ApiResponse.success(SuccessMessage.FIND_VIDEO_SUCCESS,video);
        }catch (EntityNotFoundException e) {
            return ApiResponse.error(ErrorMessage.VIDEO_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error(ErrorMessage.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public ApiResponse<VideoViewDTO> viewVideo(Long userId, Long videoId){
        if(!videoRepository.existsById(videoId)){
            return ApiResponse.error(ErrorMessage.VIDEO_NOT_FOUND, "영상이 존재하지 않습니다");
        }
        if(!usersRepository.existsById(userId)){
            return ApiResponse.error(ErrorMessage.NOT_FOUND_USER_EXCEPTION, "유저가 존재하지 않습니다");
        }

        VideoViewDTO videoViewDTO = new VideoViewDTO();
        if(!userVideoRepository.existsByVideo_VideoIdAndUsers_UsersId(videoId, userId)){
            UserVideo userVideo = new UserVideo();
            userVideo.setVideo(videoRepository.findById(videoId).get());
            userVideo.setUsers(usersRepository.findById(userId).get());
            userVideoRepository.save(userVideo);
        }
        if(userTodoVideoRepository.existsByVideo_VideoIdAndUsers_UsersId(videoId, userId)){
            UserTodoVideo userTodoVideo = userTodoVideoRepository.findByVideo_VideoIdAndUsers_UsersId(videoId, userId);
            userTodoVideo.setStage(3);
            userTodoVideoRepository.save(userTodoVideo);
        }
        videoViewDTO.setMessage("영상 보기를 완료했습니다.");
        return ApiResponse.success(SuccessMessage.VIDEO_VIEW_DONE, videoViewDTO);
    }
}
