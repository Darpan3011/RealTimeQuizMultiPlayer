package com.darpan.realtimemultiplayerquiz.exception;

/**
 * Exception thrown when a quiz is not found.
 */
public class QuizNotFoundException extends RuntimeException {

    public QuizNotFoundException(String message) {
        super(message);
    }

    public QuizNotFoundException(int quizId) {
        super("Quiz not found with ID: " + quizId);
    }

    public QuizNotFoundException(String field, Object value) {
        super("Quiz not found with " + field + ": " + value);
    }
}
