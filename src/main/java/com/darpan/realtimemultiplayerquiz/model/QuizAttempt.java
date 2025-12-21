package com.darpan.realtimemultiplayerquiz.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "quiz_attempt")
public class QuizAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    private int score;

    private int totalQuestions;

    private LocalDateTime timestamp;

    public QuizAttempt() {
        this.timestamp = LocalDateTime.now();
    }

    public QuizAttempt(Player player, Quiz quiz, int score, int totalQuestions) {
        this.player = player;
        this.quiz = quiz;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.timestamp = LocalDateTime.now();
    }

}
