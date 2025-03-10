package com.darpan.realtimemultiplayerquiz.dao;

import com.darpan.realtimemultiplayerquiz.dto.QuestionDTO;
import com.darpan.realtimemultiplayerquiz.model.Question;

public interface QuestionDAO {

    void addQuestion(QuestionDTO question);
}
