package com.darpan.realtimemultiplayerquiz.dao;

import com.darpan.realtimemultiplayerquiz.dto.QuizDTO;
import com.darpan.realtimemultiplayerquiz.model.Quiz;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizDAO extends JpaRepository<Quiz, Integer> {

    @Transactional
    @Query(value = "select * from quiz", nativeQuery = true)
    List<QuizDTO> getAllQuiz();

    @Transactional
    @Query(value = "select * from quiz where quiz_id = :id",nativeQuery = true)
    QuizDTO getQuizById(int id);
}
