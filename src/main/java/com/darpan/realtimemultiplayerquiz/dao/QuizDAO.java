package com.darpan.realtimemultiplayerquiz.dao;

import com.darpan.realtimemultiplayerquiz.dto.QuizDTO;
import com.darpan.realtimemultiplayerquiz.model.Quiz;

import java.util.List;


public interface QuizDAO {


    void addQuiz(Quiz quiz);

    List<QuizDTO> getAllQuiz();

    QuizDTO getQuizById(int id);

    QuizDTO getQuizByCode(int quizCode);

}
