package com.hacker.hackathon.controller;

import com.hacker.hackathon.common.response.ApiResponse;
import com.hacker.hackathon.dto.ListQuizDTO;
import com.hacker.hackathon.dto.ListVideoDTO;
import com.hacker.hackathon.service.MappingService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MappingController {

    private final MappingService mappingService;

    public MappingController(MappingService mappingService) {
        this.mappingService = mappingService;
    }

    @PostMapping("/list/{listId}/video/{videoId}")
    public ApiResponse<ListVideoDTO> mapVideoToList(@PathVariable Long listId, @PathVariable Long videoId){
        return mappingService.mapVideoToList(listId, videoId);
    }

    @PostMapping("/list/{listId}/quiz/{quizId}")
    public ApiResponse<ListQuizDTO> mapQuizToList(@PathVariable Long listId, @PathVariable Long quizId){
        return mappingService.mapQuizToList(listId, quizId);
    }
}
