package com.darpan.realtimemultiplayerquiz.serviceimpl;

import com.darpan.realtimemultiplayerquiz.dao.QuestionDAO;
import com.darpan.realtimemultiplayerquiz.dto.QuestionDTO;
import com.darpan.realtimemultiplayerquiz.dto.QuestionWebSocketDTO;
import com.darpan.realtimemultiplayerquiz.service.QuestionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    private QuestionDAO questionDAO;

    public QuestionServiceImpl(QuestionDAO questionDAO) {
        this.questionDAO = questionDAO;
    }
    @Override
    public void createQuestion(QuestionDTO questionDTO) {

            QuestionDTO question = new QuestionDTO();
            question.setQuestionTitle(questionDTO.getQuestionTitle());
            question.setCorrectAnswer(questionDTO.getCorrectAnswer());
            question.setOptions(questionDTO.getOptions());
            question.setQuizId(questionDTO.getQuizId());

            questionDAO.addQuestion(question);
        }

    @Override
    public List<QuestionWebSocketDTO> getAllQuestions(int quizId) {
        return questionDAO.getAllQuestions(quizId);
    }
}
