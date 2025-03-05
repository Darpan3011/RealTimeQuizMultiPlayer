package com.darpan.realtimemultiplayerquiz.dto;


import java.util.List;

public class QuestionDTO {
    private String questionTitle;
    private String correctAnswer;
    private List<String> options;
    private int quizId;  // Refers to quiz_id in JSON

    public QuestionDTO() {}

    public QuestionDTO(String questionTitle, String correctAnswer, List<String> options, int quizId) {
        this.questionTitle = questionTitle;
        this.correctAnswer = correctAnswer;
        this.options = options;
        this.quizId = quizId;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }
}
