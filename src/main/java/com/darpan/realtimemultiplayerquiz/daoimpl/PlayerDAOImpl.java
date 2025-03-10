package com.darpan.realtimemultiplayerquiz.daoimpl;

import com.darpan.realtimemultiplayerquiz.dao.PlayerDAO;
import com.darpan.realtimemultiplayerquiz.dao.QuizDAO;
import com.darpan.realtimemultiplayerquiz.dto.PlayerDTO;
import com.darpan.realtimemultiplayerquiz.dto.QuizDTO;
import com.darpan.realtimemultiplayerquiz.model.Player;
import com.darpan.realtimemultiplayerquiz.model.Quiz;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PlayerDAOImpl implements PlayerDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private QuizDAO quizDAO;

    @Override
    public void addPlayer(PlayerDTO playerDTO) {

        try(Session session = sessionFactory.openSession()) {

            session.beginTransaction();

            QuizDTO q = quizDAO.getQuizByCode(playerDTO.getQuizCode());

            Player p = new Player();
            p.setName(playerDTO.getName());
            Quiz q1 = new Quiz();
            q1.setQuizCode(q.getQuizCode());
            q1.setId(q.getQuizId());
            p.setQuizzes(List.of(q1));
            session.save(p);
            session.getTransaction().commit();
        }
    }
}
