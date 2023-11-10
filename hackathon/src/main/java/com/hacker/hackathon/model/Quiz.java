package com.hacker.hackathon.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Table
@Entity
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="quiz_id")
    private Long quizId;

    @Column
    private String question;

    @Column
    private Boolean answer;

    @Column
    private String s3Url;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<TodoQuiz> todoQuizzes = new HashSet<>();

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<UserTodoQuiz> userTodoQuizzes = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "video_id", nullable = false)
    @JsonBackReference
    private Video video;
}
