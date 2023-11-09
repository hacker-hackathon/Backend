package com.hacker.hackathon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserTodoListByIdDTO {
    private Long userTodoListId;
    private String name;
    private String description;
    private Long progress;
    private List<UserTodoVideoDTO> userTodoVideos;
    private List<UserTodoQuizDTO> userTodoQuizzes;
}
