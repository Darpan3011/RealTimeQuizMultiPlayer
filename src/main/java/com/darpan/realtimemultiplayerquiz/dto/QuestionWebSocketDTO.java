package com.darpan.realtimemultiplayerquiz.dto;


import java.util.List;

public class QuestionWebSocketDTO {

    private int id;
    private String questionTitle;
    private List<String> options;
    private String correctAnswer;

    public QuestionWebSocketDTO(String questionTitle, List<String> options, int id, String correctAnswer) {
        this.id = id;
        this.questionTitle = questionTitle;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    public QuestionWebSocketDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
