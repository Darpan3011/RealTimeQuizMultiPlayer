package com.darpan.realtimemultiplayerquiz.model;

import jakarta.persistence.*;

import java.util.List;

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

    public Quiz() {
    }

    public Quiz(int id, int quizCode, String quizName, List<Question> questions, List<Player> players) {
        this.id = id;
        this.quizCode = quizCode;
        this.quizName = quizName;
        this.questions = questions;
        this.players = players;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuizCode() {
        return quizCode;
    }

    public void setQuizCode(int quizCode) {
        this.quizCode = quizCode;
    }

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "id=" + id +
                ", quizCode=" + quizCode +
                ", quizName='" + quizName + '\'' +
                ", questions=" + questions +
                ", players=" + players +
                '}';
    }
}
