package com.hacker.hackathon.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.catalina.User;

@Getter
@Setter
@Table
@Entity
public class UserTodoList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_todo_list_id")
    private Long userTodoListId;

    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    @JsonBackReference
    private Users users;

    @Column
    private String name;

    @ManyToOne
    @JoinColumn(name = "todo_list", nullable = false)
    @JsonBackReference
    private TodoList todoList;

    @ManyToOne
    @JoinColumn(name = "user_todo_quiz", nullable = false)
    @JsonBackReference
    private UserTodoQuiz userTodoQuiz;
}
