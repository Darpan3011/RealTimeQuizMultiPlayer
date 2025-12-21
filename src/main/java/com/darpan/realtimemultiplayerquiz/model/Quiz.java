package com.darpan.realtimemultiplayerquiz.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@ToString
@Table(name = "quiz")
@Entity
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id")
    private int id;

    @Column(name = "quiz_code", unique = true, nullable = false)
    private int quizCode;

    @Column(nullable = false, name = "quiz_name")
    private String quizName;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions;

    @ManyToMany(mappedBy = "quizzes")
    private List<Player> players;

    public Quiz(int id, int quizCode, String quizName, List<Question> questions, List<Player> players) {
        this.id = id;
        this.quizCode = quizCode;
        this.quizName = quizName;
        this.questions = questions;
        this.players = players;
    }
}
