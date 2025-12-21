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
    private String correctAnswer;

    public QuestionWebSocketDTO(String questionTitle, List<String> options, int id, String correctAnswer) {
        this.id = id;
        this.questionTitle = questionTitle;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

}
