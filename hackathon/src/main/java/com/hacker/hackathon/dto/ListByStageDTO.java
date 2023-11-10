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
public class ListByStageDTO {
    private Long userTodoListId;
    private Long usersId;
    private Long stageId;
    private String userTodoListName;
    private String description;

    private Set<ListByStageVideoDTO> listByStageVideoDTOS;
}
