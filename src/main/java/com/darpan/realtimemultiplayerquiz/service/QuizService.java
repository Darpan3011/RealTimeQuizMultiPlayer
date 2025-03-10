package com.darpan.realtimemultiplayerquiz.service;

import com.darpan.realtimemultiplayerquiz.dto.QuizDTO;
import com.darpan.realtimemultiplayerquiz.model.Quiz;

import java.util.List;

public interface QuizService {

    void saveQuiz(Quiz quiz);

    List<QuizDTO> getAllQuiz();

    QuizDTO getQuizById(int id);

    QuizDTO getQuizByCode(int quizCode);
}
