package com.darpan.realtimemultiplayerquiz.service;

import com.darpan.realtimemultiplayerquiz.dto.QuestionClientDTO;
import com.darpan.realtimemultiplayerquiz.dto.QuestionDTO;
import com.darpan.realtimemultiplayerquiz.dto.QuestionWebSocketDTO;

import java.util.List;

public interface QuestionService {

    void createQuestion(QuestionDTO questionDTO);

    List<QuestionWebSocketDTO> getAllQuestions(int quizId);

    /**
     * Get questions for quiz participants (without correct answers).
     * 
     * @param quizId the quiz ID
     * @return list of questions without correct answers
     */
    List<QuestionClientDTO> getQuestionsForClients(int quizId);
}
