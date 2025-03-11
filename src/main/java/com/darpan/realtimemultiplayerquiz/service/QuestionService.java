package com.darpan.realtimemultiplayerquiz.service;

import com.darpan.realtimemultiplayerquiz.dto.QuestionDTO;
import com.darpan.realtimemultiplayerquiz.dto.QuestionWebSocketDTO;

import java.util.List;

public interface QuestionService {

    void createQuestion(QuestionDTO questionDTO);

    List<QuestionWebSocketDTO> getAllQuestions(int quizId);
}
