package com.darpan.realtimemultiplayerquiz.serviceimpl;

import com.darpan.realtimemultiplayerquiz.dao.QuizAttemptRepository;
import com.darpan.realtimemultiplayerquiz.dao.QuizRepository;
import com.darpan.realtimemultiplayerquiz.dto.LeaderboardDTO;
import com.darpan.realtimemultiplayerquiz.dto.QuizDTO;
import com.darpan.realtimemultiplayerquiz.model.Quiz;
import com.darpan.realtimemultiplayerquiz.model.QuizAttempt;
import com.darpan.realtimemultiplayerquiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;
    private final QuizAttemptRepository quizAttemptRepository;

    @Override
    public void saveQuiz(Quiz quiz) {
        quizRepository.save(quiz);
    }

    @Override
    public List<QuizDTO> getAllQuiz() {
        return quizRepository.findAll().stream()
                .map(x -> new QuizDTO(x.getId(), x.getQuizCode(), x.getQuizName()))
                .collect(Collectors.toList());
    }

    @Override
    public QuizDTO getQuizById(int id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found with ID: " + id));
        return new QuizDTO(quiz.getId(), quiz.getQuizCode(), quiz.getQuizName());
    }

    @Override
    public QuizDTO getQuizByCode(int quizCode) {
        Quiz quiz = quizRepository.findByQuizCode(quizCode)
                .orElseThrow(() -> new RuntimeException("Quiz not found with code: " + quizCode));
        return new QuizDTO(quiz.getId(), quiz.getQuizCode(), quiz.getQuizName());
    }

    @Override
    public List<LeaderboardDTO> getLeaderboard(int quizCode) {
        Quiz quiz = quizRepository.findByQuizCode(quizCode)
                .orElseThrow(() -> new RuntimeException("Quiz not found with code: " + quizCode));

        List<QuizAttempt> attempts = quizAttemptRepository
                .findTop10ByQuizIdOrderByScoreDesc(quiz.getId());

        return attempts.stream()
                .map(a -> new LeaderboardDTO(
                        a.getPlayer().getName(),
                        a.getScore(),
                        a.getTimestamp()))
                .collect(Collectors.toList());
    }
}
