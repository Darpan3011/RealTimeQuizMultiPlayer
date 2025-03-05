package com.darpan.realtimemultiplayerquiz.dto;

public class QuizDTO {

    private int quizId;
    private int quizCode;
    private String quizName;

    public QuizDTO(int quizId, int quizCode, String quizName) {
        this.quizId = quizId;
        this.quizCode = quizCode;
        this.quizName = quizName;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
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
}
