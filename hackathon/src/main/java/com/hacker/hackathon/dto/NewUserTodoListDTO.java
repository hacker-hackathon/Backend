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
public class NewUserTodoListDTO {
    private Long userTodoListId;
    private Long defaultTodoListId;
    private Long userId;
    private String userTodoListName;
    private String userTodoListDescription;
    private Set<UserTodoVideosDTO> userTodoVideos;
    private Set<UserTodoQuizsDTO> userTodoQuizsDTOS;
}
