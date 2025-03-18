package com.darpan.realtimemultiplayerquiz.daoimpl;

import com.darpan.realtimemultiplayerquiz.dao.QuestionDAO;
import com.darpan.realtimemultiplayerquiz.dto.QuestionDTO;
import com.darpan.realtimemultiplayerquiz.dto.QuestionWebSocketDTO;
import com.darpan.realtimemultiplayerquiz.dto.QuizDTO;
import com.darpan.realtimemultiplayerquiz.model.Question;
import com.darpan.realtimemultiplayerquiz.model.Quiz;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    @Override
    public List<QuestionWebSocketDTO> getAllQuestions(int quizId) {
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Question> cq = cb.createQuery(Question.class);
            Root<Question> root = cq.from(Question.class);
            cq.select(root).where(cb.equal(root.get("quiz").get("id"), quizId));
            List<Question> questions = session.createQuery(cq).list();
            session.getTransaction().commit();

            return questions.stream().map(x-> new QuestionWebSocketDTO(x.getQuestionTitle(), x.getOptions(), x.getId(), x.getCorrectAnswer())).toList();
        }
    }
}
