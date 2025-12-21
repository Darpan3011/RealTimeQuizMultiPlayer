package com.darpan.realtimemultiplayerquiz.exception;

/**
 * Exception thrown when a question is not found.
 */
public class QuestionNotFoundException extends RuntimeException {

    public QuestionNotFoundException(String message) {
        super(message);
    }

    public QuestionNotFoundException(int questionId) {
        super("Question not found with ID: " + questionId);
    }
}
