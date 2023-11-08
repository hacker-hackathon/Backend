package com.hacker.hackathon.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Table
@Entity
public class UserTodoVideo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_todo_video_id")
    private Long userTodoVideoId;

    @Column
    private Boolean completed;

    @Column
    private Date completeAt;

    @Column
    private Date completedAt;

    @ManyToOne
    @JoinColumn(name = "video_id", nullable = false)
    @JsonBackReference
    private Video video;

    @ManyToOne
    @JoinColumn(name = "user_todo_list_id", nullable = false)
    @JsonBackReference
    private UserTodoList userTodoList;


}

