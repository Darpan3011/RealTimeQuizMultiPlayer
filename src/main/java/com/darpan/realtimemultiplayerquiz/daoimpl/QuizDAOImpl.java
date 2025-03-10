package com.darpan.realtimemultiplayerquiz.daoimpl;

import com.darpan.realtimemultiplayerquiz.dao.QuizDAO;
import com.darpan.realtimemultiplayerquiz.dto.QuizDTO;
import com.darpan.realtimemultiplayerquiz.model.Quiz;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class QuizDAOImpl implements QuizDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void addQuiz(Quiz quiz) {
        System.out.println(quiz);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(quiz);
            session.getTransaction().commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<QuizDTO> getAllQuiz() {

        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Quiz> quizzes = session.createQuery("FROM Quiz" ,Quiz.class).list();
            session.getTransaction().commit();

            List<QuizDTO> quizDTOs = quizzes.stream().map(x->
                new QuizDTO(x.getId(), x.getQuizCode(), x.getQuizName())
            ).collect(Collectors.toList());

            return quizDTOs;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public QuizDTO getQuizById(int id) {
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Quiz> cq = cb.createQuery(Quiz.class);
            Root<Quiz> root = cq.from(Quiz.class);
            cq.select(root).where(cb.equal(root.get("id"), id));
            Quiz quiz = session.createQuery(cq).uniqueResult();
            session.getTransaction().commit();
            return new QuizDTO(quiz.getId(), quiz.getQuizCode(), quiz.getQuizName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public QuizDTO getQuizByCode(int quizCode) {

        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Quiz> cq = cb.createQuery(Quiz.class);
            Root<Quiz> root = cq.from(Quiz.class);
            cq.select(root).where(cb.equal(root.get("quizCode"), quizCode));
            Quiz quiz = session.createQuery(cq).uniqueResult();
            session.getTransaction().commit();
            return new QuizDTO(quiz.getId(), quiz.getQuizCode(), quiz.getQuizName());
        }
    }


}
