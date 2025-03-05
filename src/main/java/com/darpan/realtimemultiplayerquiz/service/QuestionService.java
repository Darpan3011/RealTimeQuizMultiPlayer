package com.darpan.realtimemultiplayerquiz.service;

import com.darpan.realtimemultiplayerquiz.dto.QuestionDTO;
import com.darpan.realtimemultiplayerquiz.model.Question;
import org.springframework.stereotype.Service;

@Service
public interface QuestionService {

    void createQuestion(QuestionDTO questionDTO);
}
