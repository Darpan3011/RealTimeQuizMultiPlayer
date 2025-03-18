package com.darpan.realtimemultiplayerquiz.dto;

import java.util.List;

public class SubmitAnswerDTO {
    private int quizCode;
    private String playerName;
    private List<String> answers;

    public SubmitAnswerDTO(int quizCode, String playerName, List<String> answers) {
        this.quizCode = quizCode;
        this.playerName = playerName;
        this.answers = answers;
    }

    public SubmitAnswerDTO() {
    }

    public int getQuizCode() {
        return quizCode;
    }

    public void setQuizCode(int quizCode) {
        this.quizCode = quizCode;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }
}
