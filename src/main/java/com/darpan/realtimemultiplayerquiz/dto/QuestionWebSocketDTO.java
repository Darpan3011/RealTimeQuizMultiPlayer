package com.darpan.realtimemultiplayerquiz.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class QuestionWebSocketDTO {

    private int id;
    private String questionTitle;
    private List<String> options;
    private int correctAnswerIndex;

    public QuestionWebSocketDTO(String questionTitle, List<String> options, int id, int correctAnswerIndex) {
        this.id = id;
        this.questionTitle = questionTitle;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
    }

}
