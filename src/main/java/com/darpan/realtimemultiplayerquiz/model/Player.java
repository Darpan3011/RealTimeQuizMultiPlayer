package com.darpan.realtimemultiplayerquiz.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Table(name = "player")
@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany
    @JoinTable(name = "player_quiz", joinColumns = @JoinColumn(name = "player_id"), inverseJoinColumns = @JoinColumn(name = "quiz_id"))
    private List<Quiz> quizzes;

    public Player(int id, String name, List<Quiz> quizzes) {
        this.id = id;
        this.name = name;
        this.quizzes = quizzes;
    }

}
