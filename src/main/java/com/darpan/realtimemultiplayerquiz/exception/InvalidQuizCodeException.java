package com.darpan.realtimemultiplayerquiz.exception;

/**
 * Exception thrown when an invalid quiz code is provided.
 */
public class InvalidQuizCodeException extends RuntimeException {

    public InvalidQuizCodeException(String message) {
        super(message);
    }

    public InvalidQuizCodeException(int quizCode) {
        super("Invalid quiz code: " + quizCode);
    }
}
