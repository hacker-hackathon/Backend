package com.hacker.hackathon.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hacker.hackathon.dto.authorization.request.SignupRequestDTO;
import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Table
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="users_id")
    private Long usersId;

    @Column
    private String name;

    @Column
    private String password;

    @Column
    @NotNull
    private String email;

    @Column
    private String nickname;

    @Column
    private String social;

    @Column
    private String profileImageUrl;

    @Column(length = 1500)
    private String introduce;

    @Column
    private Long totalScore = 0L;

    @Column
    @CreatedDate
    private LocalDateTime createdAt;
    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<UserVideo> userVideos = new HashSet<>();

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<UserTodoList> userTodoLists = new HashSet<>();

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<UserTodoQuiz> userTodoQuizzes = new HashSet<>();

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<UserTodoVideo> userTodoVideos = new HashSet<>();

    public static Users of(SignupRequestDTO requestDTO) {
        return Users.builder()
                .name(requestDTO.getName())
                .nickname(requestDTO.getNickname())
                .email(requestDTO.getEmail())
                .introduce(requestDTO.getIntroduce())
                .password(requestDTO.getPassword())
                .totalScore(0L)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
