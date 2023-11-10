package com.hacker.hackathon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserTodoQuizzesDTO {
    private Long quizId;
    private Long userTodoQuizId;
    private String question;
    private Boolean answer;
    private Boolean previousAnswer;
}
