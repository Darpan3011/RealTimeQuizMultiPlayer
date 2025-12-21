package com.darpan.realtimemultiplayerquiz.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for creating a new quiz.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateQuizDTO {

    @Positive(message = "Quiz code must be positive")
    private int quizCode;

    @NotBlank(message = "Quiz name is required")
    @Size(min = 3, max = 200, message = "Quiz name must be between 3 and 200 characters")
    private String quizName;
}
