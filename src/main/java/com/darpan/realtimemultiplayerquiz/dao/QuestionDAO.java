package com.darpan.realtimemultiplayerquiz.dao;

import com.darpan.realtimemultiplayerquiz.dto.QuestionDTO;
import com.darpan.realtimemultiplayerquiz.dto.QuestionWebSocketDTO;
import com.darpan.realtimemultiplayerquiz.model.Question;

import java.util.List;

public interface QuestionDAO {

    void addQuestion(QuestionDTO question);

    List<QuestionWebSocketDTO> getAllQuestions(int quizId);
}
