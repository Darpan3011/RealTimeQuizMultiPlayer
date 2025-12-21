package com.darpan.realtimemultiplayerquiz.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class QuestionDTO {
    private String questionTitle;
    private String correctAnswer;
    private List<String> options;
    private int quizId;  // Refers to quiz_id in JSON

    public QuestionDTO(String questionTitle, String correctAnswer, List<String> options, int quizId) {
        this.questionTitle = questionTitle;
        this.correctAnswer = correctAnswer;
        this.options = options;
        this.quizId = quizId;
    }

}
