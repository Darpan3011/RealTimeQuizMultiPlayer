package com.darpan.realtimemultiplayerquiz.serviceimpl;

import com.darpan.realtimemultiplayerquiz.dao.QuizAttemptRepository;
import com.darpan.realtimemultiplayerquiz.dao.QuizRepository;
import com.darpan.realtimemultiplayerquiz.dto.LeaderboardDTO;
import com.darpan.realtimemultiplayerquiz.dto.QuizDTO;
import com.darpan.realtimemultiplayerquiz.exception.QuizNotFoundException;
import com.darpan.realtimemultiplayerquiz.model.Quiz;
import com.darpan.realtimemultiplayerquiz.model.QuizAttempt;
import com.darpan.realtimemultiplayerquiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuizServiceImpl implements QuizService {

        private final QuizRepository quizRepository;
        private final QuizAttemptRepository quizAttemptRepository;

        @Override
        @Transactional
        public void saveQuiz(Quiz quiz) {
                log.info("Saving new quiz with code: {} and name: {}", quiz.getQuizCode(), quiz.getQuizName());
                quizRepository.save(quiz);
                log.debug("Quiz saved successfully with ID: {}", quiz.getId());
        }

        @Override
        @Transactional(readOnly = true)
        public List<QuizDTO> getAllQuiz() {
                log.debug("Fetching all quizzes");
                List<QuizDTO> quizzes = quizRepository.findAll().stream()
                                .map(x -> new QuizDTO(x.getId(), x.getQuizCode(), x.getQuizName()))
                                .collect(Collectors.toList());
                log.info("Retrieved {} quizzes", quizzes.size());
                return quizzes;
        }

        @Override
        @Transactional(readOnly = true)
        public QuizDTO getQuizById(int id) {
                log.debug("Fetching quiz by ID: {}", id);
                Quiz quiz = quizRepository.findById(id)
                                .orElseThrow(() -> new QuizNotFoundException(id));
                return new QuizDTO(quiz.getId(), quiz.getQuizCode(), quiz.getQuizName());
        }

        @Override
        @Transactional(readOnly = true)
        public QuizDTO getQuizByCode(int quizCode) {
                log.debug("Fetching quiz by code: {}", quizCode);
                Quiz quiz = quizRepository.findByQuizCode(quizCode)
                                .orElseThrow(() -> new QuizNotFoundException("code", quizCode));
                return new QuizDTO(quiz.getId(), quiz.getQuizCode(), quiz.getQuizName());
        }

        @Override
        @Transactional(readOnly = true)
        public List<LeaderboardDTO> getLeaderboard(int quizCode) {
                log.debug("Fetching leaderboard for quiz code: {}", quizCode);
                Quiz quiz = quizRepository.findByQuizCode(quizCode)
                                .orElseThrow(() -> new QuizNotFoundException("code", quizCode));

                List<QuizAttempt> attempts = quizAttemptRepository
                                .findTop10ByQuizIdOrderByScoreDesc(quiz.getId());

                List<LeaderboardDTO> leaderboard = attempts.stream()
                                .map(a -> new LeaderboardDTO(
                                                a.getPlayer().getName(),
                                                a.getScore(),
                                                a.getTimestamp()))
                                .collect(Collectors.toList());

                log.info("Retrieved leaderboard with {} entries for quiz code: {}", leaderboard.size(), quizCode);
                return leaderboard;
        }
}
