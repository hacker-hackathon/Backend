package com.hacker.hackathon.controller;

import com.hacker.hackathon.common.response.ApiResponse;
import com.hacker.hackathon.common.response.ErrorMessage;
import com.hacker.hackathon.common.response.SuccessMessage;
import com.hacker.hackathon.dto.TranscriptDTO;
import com.hacker.hackathon.dto.TranscriptDataDTO;
import com.hacker.hackathon.service.TranscriptService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TranscriptController {

    private final TranscriptService transcriptService;

    public TranscriptController(TranscriptService transcriptService) {
        this.transcriptService = transcriptService;
    }

    @GetMapping("/videos/{videoId}/transcripts")
    public ApiResponse<TranscriptDataDTO> getTranscriptsForVideo(@PathVariable("videoId") Long id) {
        try {
            TranscriptDataDTO data = transcriptService.getTranscriptsByVideoId(id);
            return ApiResponse.success(SuccessMessage.GET_TRANSCRIPT_SUCCESS, data);
        } catch (EntityNotFoundException e) {
            return ApiResponse.error(ErrorMessage.TRANSCRIPT_NOT_FOUND, e.getMessage());
        }
    }


    @GetMapping("/videos/{videoId}/transcripts/{transcriptId}")
    public ApiResponse<TranscriptDTO> getTranscriptById(@PathVariable("videoId") Long videoId, @PathVariable("transcriptId") Long transcriptId) {
        try {
            TranscriptDTO data = transcriptService.getTranscriptByVideoIdAndTranscriptId(videoId, transcriptId);
            return ApiResponse.success(SuccessMessage.GET_TRANSCRIPT_SUCCESS, data);
        } catch (EntityNotFoundException e) {
            return ApiResponse.error(ErrorMessage.TRANSCRIPT_NOT_FOUND, e.getMessage());
        }
    }
}
