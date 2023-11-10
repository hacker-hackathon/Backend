package com.hacker.hackathon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuizDTO {
    private Long quizId;
    private String question;
    private Boolean answer;
    private String s3Url;
}
