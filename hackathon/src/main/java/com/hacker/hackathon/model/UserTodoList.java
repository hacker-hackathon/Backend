package com.hacker.hackathon.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.catalina.User;

import java.util.HashSet;
import java.util.Set;

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

    @Column(length = 1000)
    private String description;

    @Column
    private Long progress;
    @ManyToOne
    @JoinColumn(name = "todo_list_id", nullable = false)
    @JsonBackReference
    private TodoList todoList;

    @OneToMany(mappedBy = "userTodoList", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<UserTodoQuiz> userTodoQuizzes = new HashSet<>();

    @OneToMany(mappedBy = "userTodoList", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<UserTodoVideo> userTodoVideos = new HashSet<>();

}
