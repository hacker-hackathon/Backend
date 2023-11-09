package com.hacker.hackathon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserTodoVideoDTO {
    private Long userTodoVideoId;
    private Long stage;
    private Date completeAt;
    private Date completedAt;
    private Long videoId;
}
