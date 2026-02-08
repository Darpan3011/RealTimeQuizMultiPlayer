package com.darpan.realtimemultiplayerquiz.exception;

import com.darpan.realtimemultiplayerquiz.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.stream.Collectors;

/**
 * Global exception handler for the application.
 * Provides consistent error responses across all controllers.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(QuizNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleQuizNotFoundException(
                        QuizNotFoundException ex, WebRequest request) {
                ErrorResponse error = new ErrorResponse(
                                HttpStatus.NOT_FOUND.value(),
                                "Quiz Not Found",
                                ex.getMessage());
                error.setPath(request.getDescription(false).replace("uri=", ""));
                return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(PlayerNotFoundException.class)
        public ResponseEntity<ErrorResponse> handlePlayerNotFoundException(
                        PlayerNotFoundException ex, WebRequest request) {
                ErrorResponse error = new ErrorResponse(
                                HttpStatus.NOT_FOUND.value(),
                                "Player Not Found",
                                ex.getMessage());
                error.setPath(request.getDescription(false).replace("uri=", ""));
                return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(QuestionNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleQuestionNotFoundException(
                        QuestionNotFoundException ex, WebRequest request) {
                ErrorResponse error = new ErrorResponse(
                                HttpStatus.NOT_FOUND.value(),
                                "Question Not Found",
                                ex.getMessage());
                error.setPath(request.getDescription(false).replace("uri=", ""));
                return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<ErrorResponse> handleRuntimeException(
                        RuntimeException ex, WebRequest request) {
                ErrorResponse error = new ErrorResponse(
                                HttpStatus.BAD_REQUEST.value(),
                                "Bad Request",
                                ex.getMessage());
                error.setPath(request.getDescription(false).replace("uri=", ""));
                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleValidationException(
                        MethodArgumentNotValidException ex, WebRequest request) {
                String errors = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                                .collect(Collectors.joining(", "));

                ErrorResponse error = new ErrorResponse(
                                HttpStatus.BAD_REQUEST.value(),
                                "Validation Failed",
                                errors);
                error.setPath(request.getDescription(false).replace("uri=", ""));
                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleGlobalException(
                        Exception ex, WebRequest request) {
                ErrorResponse error = new ErrorResponse(
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                "Internal Server Error",
                                ex.getMessage());
                error.setPath(request.getDescription(false).replace("uri=", ""));
                return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
}
