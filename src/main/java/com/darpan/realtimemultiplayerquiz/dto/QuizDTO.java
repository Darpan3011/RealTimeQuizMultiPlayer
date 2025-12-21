package com.darpan.realtimemultiplayerquiz.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QuizDTO {

    private int quizId;
    private int quizCode;
    private String quizName;

    public QuizDTO(int quizId, int quizCode, String quizName) {
        this.quizId = quizId;
        this.quizCode = quizCode;
        this.quizName = quizName;
    }

}
