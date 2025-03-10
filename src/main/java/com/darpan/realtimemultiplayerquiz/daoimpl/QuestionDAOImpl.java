package com.darpan.realtimemultiplayerquiz.daoimpl;

import com.darpan.realtimemultiplayerquiz.dao.QuestionDAO;
import com.darpan.realtimemultiplayerquiz.dto.QuestionDTO;
import com.darpan.realtimemultiplayerquiz.model.Question;
import com.darpan.realtimemultiplayerquiz.model.Quiz;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class QuestionDAOImpl implements QuestionDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void addQuestion(QuestionDTO questionDTO) {

        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Question question = new Question();
            question.setQuestionTitle(questionDTO.getQuestionTitle());
            question.setCorrectAnswer(questionDTO.getCorrectAnswer());
            question.setOptions(questionDTO.getOptions());

            Quiz quiz = session.get(Quiz.class, questionDTO.getQuizId());
            question.setQuiz(quiz);
            session.save(question);

            session.getTransaction().commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
