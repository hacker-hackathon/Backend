package com.hacker.hackathon.dto;

import com.hacker.hackathon.model.TodoQuiz;
import com.hacker.hackathon.model.TodoVideo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TodoListAllDTO {
    private Long todoListId;
    private String name;
    private String description;
    private Set<Long> todoVideos;
    private Set<Long> todoQuizzes;
}
