package com.darpan.realtimemultiplayerquiz.service;

import com.darpan.realtimemultiplayerquiz.dto.QuizDTO;
import com.darpan.realtimemultiplayerquiz.model.Quiz;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QuizService {

    void saveQuiz(Quiz quiz);

    List<QuizDTO> getAllQuiz();

    QuizDTO getQuizById(int id);
}
