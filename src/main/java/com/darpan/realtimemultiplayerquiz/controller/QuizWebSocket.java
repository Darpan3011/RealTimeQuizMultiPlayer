package com.darpan.realtimemultiplayerquiz.controller;

import com.darpan.realtimemultiplayerquiz.dao.PlayerRepository;
import com.darpan.realtimemultiplayerquiz.dao.QuizAttemptRepository;
import com.darpan.realtimemultiplayerquiz.dao.QuizRepository;
import com.darpan.realtimemultiplayerquiz.dto.LeaderboardDTO;
import com.darpan.realtimemultiplayerquiz.dto.QuestionClientDTO;
import com.darpan.realtimemultiplayerquiz.dto.QuestionWebSocketDTO;
import com.darpan.realtimemultiplayerquiz.model.Player;
import com.darpan.realtimemultiplayerquiz.model.Quiz;
import com.darpan.realtimemultiplayerquiz.model.QuizAttempt;
import com.darpan.realtimemultiplayerquiz.service.QuestionService;
import com.darpan.realtimemultiplayerquiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class QuizWebSocket {

    private final QuestionService questionService;
    private final SimpMessagingTemplate messagingTemplate;
    private final QuizAttemptRepository quizAttemptRepository;
    private final PlayerRepository playerRepository;
    private final QuizRepository quizRepository;
    private final QuizService quizService;

    // We don't use @SendTo here because we want to send to a specific quiz topic
    // based on the ID
    @MessageMapping("/fetch/{quizCode}")
    public void fetchQuizQuestions(@DestinationVariable int quizCode) {
        Quiz quiz = quizRepository.findByQuizCode(quizCode).orElse(null);

        if (quiz != null) {
            List<QuestionClientDTO> questions = questionService.getQuestionsForClients(quiz.getId());
            // Send to specific topic for this quiz
            messagingTemplate.convertAndSend("/topic/quiz/" + quizCode, questions);
        } else {
            messagingTemplate.convertAndSend("/topic/quiz/" + quizCode, "Error: Quiz not found");
        }
    }

    @MessageMapping("/submit/{quizCode}/{playerName}")
    @Transactional
    public void submitAnswers(@DestinationVariable int quizCode, @DestinationVariable String playerName,
            List<String> answers) {

        // First, get the quiz by code
        Quiz quiz = quizRepository.findByQuizCode(quizCode).orElse(null);

        if (quiz == null) {
            messagingTemplate.convertAndSend("/topic/quiz/" + quizCode, "Error: Quiz not found.");
            return;
        }

        // Get questions using quiz ID (not code)
        List<QuestionWebSocketDTO> questions = questionService.getAllQuestions(quiz.getId());

        if (questions == null || questions.isEmpty()) {
            messagingTemplate.convertAndSend("/topic/quiz/" + quizCode, "Error: No questions found.");
            return;
        }

        if (answers.size() != questions.size()) {
            messagingTemplate.convertAndSend("/topic/quiz/" + quizCode,
                    "Error: Answer count mismatch for player " + playerName);
            return;
        }

        int correctCount = 0;
        for (int i = 0; i < questions.size(); i++) {
            // Get the correct answer text using the index
            QuestionWebSocketDTO question = questions.get(i);
            if (question.getCorrectAnswerIndex() >= 0
                    && question.getCorrectAnswerIndex() < question.getOptions().size()) {
                String correctAnswerText = question.getOptions().get(question.getCorrectAnswerIndex());
                if (correctAnswerText != null && correctAnswerText.equalsIgnoreCase(answers.get(i))) {
                    correctCount++;
                }
            }
        }

        // Save Result to DB
        try {
            // Handle potential race condition for player creation
            Player player;
            try {
                player = playerRepository.findByName(playerName).orElseGet(() -> {
                    Player newPlayer = new Player();
                    newPlayer.setName(playerName);
                    return playerRepository.save(newPlayer);
                });
            } catch (DataIntegrityViolationException e) {
                // Player was created by another thread, fetch it
                log.warn("Race condition detected for player: {}, fetching existing player", playerName);
                player = playerRepository.findByName(playerName)
                        .orElseThrow(() -> new RuntimeException("Failed to retrieve player: " + playerName));
            }

            QuizAttempt attempt = new QuizAttempt(player, quiz, correctCount, questions.size());
            quizAttemptRepository.save(attempt);

            // Broadcast Leaderboard
            List<LeaderboardDTO> leaderboard = quizService.getLeaderboard(quizCode);
            messagingTemplate.convertAndSend("/topic/leaderboard/" + quizCode, leaderboard);

            // Send success confirmation to all players
            messagingTemplate.convertAndSend("/topic/quiz/" + quizCode,
                    "Player " + playerName + " completed with score: " + correctCount + "/" + questions.size());

            log.info("Quiz submission successful for player: {} on quiz: {}, score: {}/{}",
                    playerName, quizCode, correctCount, questions.size());

        } catch (Exception e) {
            log.error("Error saving quiz result for player: {} on quiz: {}", playerName, quizCode, e);
            // Send error message to client so they know submission failed
            messagingTemplate.convertAndSend("/topic/quiz/" + quizCode,
                    "Error: Failed to save results for player " + playerName + ". Please try again.");
        }
    }
}
