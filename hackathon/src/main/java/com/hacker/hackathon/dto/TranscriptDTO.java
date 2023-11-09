package com.hacker.hackathon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TranscriptDTO {

    private Long transcriptId;
    private String sentence;
    private Double startTime;
    private Double duration;
}