package com.darpan.realtimemultiplayerquiz.controller;

import com.darpan.realtimemultiplayerquiz.dto.QuestionWebSocketDTO;
import com.darpan.realtimemultiplayerquiz.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class QuizWebSocket {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private com.darpan.realtimemultiplayerquiz.dao.QuizAttemptRepository quizAttemptRepository;

    @Autowired
    private com.darpan.realtimemultiplayerquiz.dao.PlayerRepository playerRepository;

    @Autowired
    private com.darpan.realtimemultiplayerquiz.dao.QuizRepository quizRepository;

    @Autowired
    private com.darpan.realtimemultiplayerquiz.service.QuizService quizService;

    private final Map<Integer, Map<String, String>> playerAnswers = new HashMap<>();

    // We don't use @SendTo here because we want to send to a specific quiz topic
    // based on the ID
    @MessageMapping("/fetch/{quizCode}")
    public void fetchQuizQuestions(@org.springframework.messaging.handler.annotation.DestinationVariable int quizCode) {
        com.darpan.realtimemultiplayerquiz.model.Quiz quiz = quizRepository.findByQuizCode(quizCode)
                .orElse(null);

        if (quiz != null) {
            List<QuestionWebSocketDTO> questions = questionService.getAllQuestions(quiz.getId());
            // Send to specific topic for this quiz
            messagingTemplate.convertAndSend("/topic/quiz/" + quizCode, questions);
        } else {
            System.err.println("Quiz not found for code: " + quizCode);
            messagingTemplate.convertAndSend("/topic/quiz/" + quizCode, "Error: Quiz not found");
        }
    }

    @MessageMapping("/submit/{quizCode}/{playerName}")
    @org.springframework.transaction.annotation.Transactional
    public void submitAnswers(@org.springframework.messaging.handler.annotation.DestinationVariable int quizCode,
            @org.springframework.messaging.handler.annotation.DestinationVariable String playerName,
            List<String> answers) {

        // First, get the quiz by code
        com.darpan.realtimemultiplayerquiz.model.Quiz quiz = quizRepository.findByQuizCode(quizCode)
                .orElse(null);

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
            // Basic check - in a real app ensure null safety
            if (questions.get(i).getCorrectAnswer() != null
                    && questions.get(i).getCorrectAnswer().equalsIgnoreCase(answers.get(i))) {
                correctCount++;
            }
        }

        // Save Result to DB
        try {
            com.darpan.realtimemultiplayerquiz.model.Player player = playerRepository.findByName(playerName)
                    .orElseGet(() -> {
                        com.darpan.realtimemultiplayerquiz.model.Player newPlayer = new com.darpan.realtimemultiplayerquiz.model.Player();
                        newPlayer.setName(playerName);
                        newPlayer.setScore(0); // overall score, maybe optional
                        return playerRepository.save(newPlayer);
                    });

            com.darpan.realtimemultiplayerquiz.model.QuizAttempt attempt = new com.darpan.realtimemultiplayerquiz.model.QuizAttempt(
                    player, quiz, correctCount, questions.size());
            quizAttemptRepository.save(attempt);

            // Broadcast Leaderboard
            List<com.darpan.realtimemultiplayerquiz.dto.LeaderboardDTO> leaderboard = quizService
                    .getLeaderboard(quizCode);
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
        messagingTemplate.convertAndSend("/topic/quiz/" + quizCode,
                "Player " + playerName + " Score: " + correctCount + "/" + questions.size());
    }
}
