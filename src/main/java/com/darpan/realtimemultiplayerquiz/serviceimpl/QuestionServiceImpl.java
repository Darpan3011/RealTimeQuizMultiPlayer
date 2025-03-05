package com.darpan.realtimemultiplayerquiz.serviceimpl;

import com.darpan.realtimemultiplayerquiz.dao.QuestionDAO;
import com.darpan.realtimemultiplayerquiz.dto.QuestionDTO;
import com.darpan.realtimemultiplayerquiz.model.Question;
import com.darpan.realtimemultiplayerquiz.model.Quiz;
import com.darpan.realtimemultiplayerquiz.service.QuestionService;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;

@Service
public class QuestionServiceImpl implements QuestionService {

    private QuestionDAO questionDAO;

    private EntityManager entityManager;

    public QuestionServiceImpl(QuestionDAO questionDAO, EntityManager entityManager) {
        this.questionDAO = questionDAO;
        this.entityManager = entityManager;
    }


    @Override
    public void createQuestion(QuestionDTO questionDTO) {

        Quiz quiz = entityManager.getReference(Quiz.class, questionDTO.getQuizId());
        quiz.setId(questionDTO.getQuizId());
        Question question = new Question();
        question.setQuestionTitle(questionDTO.getQuestionTitle());
        question.setCorrectAnswer(questionDTO.getCorrectAnswer());
        question.setOptions(questionDTO.getOptions());
        question.setQuiz(quiz);

        questionDAO.save(question);
    }
}
