package com.darpan.realtimemultiplayerquiz.dto;

public class PlayerDTO {

    private int quizCode;
    private String name;

    public PlayerDTO(int quizCode, String name) {
        this.quizCode = quizCode;
        this.name = name;
    }

    public PlayerDTO() {
    }

    public int getQuizCode() {
        return quizCode;
    }

    public void setQuizCode(int quizCode) {
        this.quizCode = quizCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
