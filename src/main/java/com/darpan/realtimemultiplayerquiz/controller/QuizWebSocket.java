package com.darpan.realtimemultiplayerquiz.controller;

import com.darpan.realtimemultiplayerquiz.dao.PlayerRepository;
import com.darpan.realtimemultiplayerquiz.dao.QuizAttemptRepository;
import com.darpan.realtimemultiplayerquiz.dao.QuizRepository;
import com.darpan.realtimemultiplayerquiz.dto.LeaderboardDTO;
import com.darpan.realtimemultiplayerquiz.dto.QuestionWebSocketDTO;
import com.darpan.realtimemultiplayerquiz.model.Player;
import com.darpan.realtimemultiplayerquiz.model.Quiz;
import com.darpan.realtimemultiplayerquiz.model.QuizAttempt;
import com.darpan.realtimemultiplayerquiz.service.QuestionService;
import com.darpan.realtimemultiplayerquiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class QuizWebSocket {

    private final QuestionService questionService;
    private final SimpMessagingTemplate messagingTemplate;
    private final QuizAttemptRepository quizAttemptRepository;
    private final PlayerRepository playerRepository;
    private final QuizRepository quizRepository;
    private final QuizService quizService;
    private final Map<Integer, Map<String, String>> playerAnswers = new HashMap<>();

    // We don't use @SendTo here because we want to send to a specific quiz topic
    // based on the ID
    @MessageMapping("/fetch/{quizCode}")
    public void fetchQuizQuestions(@DestinationVariable int quizCode) {
        Quiz quiz = quizRepository.findByQuizCode(quizCode).orElse(null);

        if (quiz != null) {
            List<QuestionWebSocketDTO> questions = questionService.getAllQuestions(quiz.getId());
            // Send to specific topic for this quiz
            messagingTemplate.convertAndSend("/topic/quiz/" + quizCode, questions);
        } else {
            messagingTemplate.convertAndSend("/topic/quiz/" + quizCode, "Error: Quiz not found");
        }
    }

    @MessageMapping("/submit/{quizCode}/{playerName}")
    @Transactional
    public void submitAnswers(@DestinationVariable int quizCode, @DestinationVariable String playerName, List<String> answers) {

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
            messagingTemplate.convertAndSend("/topic/quiz/" + quizCode, "Error: Answer count mismatch for player " + playerName);
            return;
        }

        int correctCount = 0;
        for (int i = 0; i < questions.size(); i++) {
            // Basic check - in a real app ensure null safety
            if (questions.get(i).getCorrectAnswer() != null && questions.get(i).getCorrectAnswer().equalsIgnoreCase(answers.get(i))) {
                correctCount++;
            }
        }

        // Save Result to DB
        try {
            Player player = playerRepository.findByName(playerName).orElseGet(() -> {
                Player newPlayer = new Player();
                newPlayer.setName(playerName);
                newPlayer.setScore(0); // overall score, maybe optional
                return playerRepository.save(newPlayer);
            });

            QuizAttempt attempt = new QuizAttempt(player, quiz, correctCount, questions.size());
            quizAttemptRepository.save(attempt);

            // Broadcast Leaderboard
            List<LeaderboardDTO> leaderboard = quizService.getLeaderboard(quizCode);
            messagingTemplate.convertAndSend("/topic/leaderboard/" + quizCode, leaderboard);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error saving result: " + e.getMessage());
            // Proceed to broadcast anyway so user sees result
        }

        // Store player results (In-memory for now - keeping it for immediate display
        // consistency if needed, but DB is source of truth)
        playerAnswers.putIfAbsent(quizCode, new HashMap<>());
        playerAnswers.get(quizCode).put(playerName, correctCount + "/" + questions.size());

        // Send results to all players in the session
        messagingTemplate.convertAndSend("/topic/quiz/" + quizCode, "Player " + playerName + " Score: " + correctCount + "/" + questions.size());
    }
}
