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
@Table(name = "question")
@Entity
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private int id;

    @Column(name = "question_title")
    private String questionTitle;
    @Column(name = "correct_answer_index")
    private int correctAnswerIndex;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> options;

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    public Question(int id, String questionTitle, int correctAnswerIndex, List<String> options, Quiz quiz) {
        this.id = id;
        this.questionTitle = questionTitle;
        this.correctAnswerIndex = correctAnswerIndex;
        this.options = options;
        this.quiz = quiz;
    }
}
