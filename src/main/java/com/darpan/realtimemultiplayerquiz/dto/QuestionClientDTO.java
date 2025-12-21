package com.darpan.realtimemultiplayerquiz.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * DTO for sending questions to quiz participants.
 * Does NOT include the correct answer to prevent cheating.
 */
@Setter
@Getter
@NoArgsConstructor
public class QuestionClientDTO {

    private int id;
    private String questionTitle;
    private List<String> options;

    public QuestionClientDTO(int id, String questionTitle, List<String> options) {
        this.id = id;
        this.questionTitle = questionTitle;
        this.options = options;
    }
}
