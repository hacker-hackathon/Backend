package com.hacker.hackathon.controller;

import com.hacker.hackathon.common.response.ApiResponse;
import com.hacker.hackathon.dto.VideoViewDTO;
import com.hacker.hackathon.model.UserTodoVideo;
import com.hacker.hackathon.model.UserVideo;
import com.hacker.hackathon.model.Video;
import com.hacker.hackathon.service.VideoService;
import org.springframework.web.bind.annotation.*;

@RestController

public class VideoController {
    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @PostMapping("/quiz")
    public ApiResponse<Video>  uploadVideo(@RequestParam String link){
        return videoService.uploadVideo(link);
    }

    @GetMapping("/video/{videoId}")
    public ApiResponse<Video> getVideo(@PathVariable Long videoId){
        return videoService.getVideo(videoId);
    }

    @PostMapping("/user/{userId}/video/{videoId}")
    public ApiResponse<VideoViewDTO> viewVideo(@PathVariable Long userId, @PathVariable Long videoId){
        return videoService.viewVideo(userId, videoId);
    }

    @GetMapping("/user/{userId}/video/{videoId}")
    public ApiResponse<UserTodoVideo> getVideoByUserId(@PathVariable Long userId, @PathVariable Long videoId){
        return videoService.getVideoByUserId(userId, videoId);
    }
}
