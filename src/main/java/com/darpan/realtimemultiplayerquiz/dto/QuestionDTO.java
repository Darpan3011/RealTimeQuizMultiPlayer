package com.darpan.realtimemultiplayerquiz.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class QuestionDTO {

    @NotBlank(message = "Question title is required")
    @Size(min = 5, max = 500, message = "Question title must be between 5 and 500 characters")
    private String questionTitle;

    @Min(value = 0, message = "Correct answer index must be at least 0")
    @Max(value = 5, message = "Correct answer index must be at most 5")
    private int correctAnswerIndex;

    @NotEmpty(message = "At least one option is required")
    @Size(min = 2, max = 6, message = "Must have between 2 and 6 options")
    private List<String> options;

    @Positive(message = "Quiz ID must be positive")
    private int quizId; // Refers to quiz_id in JSON

    public QuestionDTO(String questionTitle, int correctAnswerIndex, List<String> options, int quizId) {
        this.questionTitle = questionTitle;
        this.correctAnswerIndex = correctAnswerIndex;
        this.options = options;
        this.quizId = quizId;
    }

}
