package com.hacker.hackathon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ListByStageVideoDTO {
    private Long userTodoVideoId;
    private String link;
    private String title;
    private String creator;
    private Set<UserTodoQuizzesDTO> userTodoQuizzesDTOS;
}
