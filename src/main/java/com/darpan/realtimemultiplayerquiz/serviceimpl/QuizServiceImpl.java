package com.darpan.realtimemultiplayerquiz.serviceimpl;

import com.darpan.realtimemultiplayerquiz.dao.QuizDAO;
import com.darpan.realtimemultiplayerquiz.dto.QuizDTO;
import com.darpan.realtimemultiplayerquiz.model.Quiz;
import com.darpan.realtimemultiplayerquiz.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizServiceImpl implements QuizService {

    @Autowired
    private QuizDAO quizDAO;

    public QuizServiceImpl(QuizDAO quizDAO) {
        this.quizDAO = quizDAO;
    }

    @Override
    public void saveQuiz(Quiz quiz) {
        quizDAO.addQuiz(quiz);
    }

    @Override
    public List<QuizDTO> getAllQuiz() {
        return quizDAO.getAllQuiz();
    }

    @Override
    public QuizDTO getQuizById(int id) {
        return quizDAO.getQuizById(id);
    }

    @Override
    public QuizDTO getQuizByCode(int quizCode) {
        return quizDAO.getQuizByCode(quizCode);
    }
}
