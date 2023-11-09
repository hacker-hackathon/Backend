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
public class UserTodoQuiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_todo_quiz_id")
    private Long userTodoQuizId;

    @Column
    private Long stage;

    @Column
    private Boolean previousAnswer;

    @Column
    private Date completeAt;

    @Column
    private Date completedAt;

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    @JsonBackReference
    private Quiz quiz;

    @ManyToOne
    @JoinColumn(name = "user_todo_list_id", nullable = false)
    @JsonBackReference
    private UserTodoList userTodoList;

    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    @JsonBackReference
    private Users users;
}


