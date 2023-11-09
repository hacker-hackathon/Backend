package com.hacker.hackathon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserTodoQuizDTO {
    private Long userTodoQuizId;
    private Long stage;
    private Boolean previousAnswer;
    private Date completeAt;
    private Date completedAt;
    private Long quizId;
}
