package com.darpan.realtimemultiplayerquiz.controller;

import com.darpan.realtimemultiplayerquiz.dto.QuestionWebSocketDTO;
import com.darpan.realtimemultiplayerquiz.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
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

    private final Map<Integer, Map<String, String>> playerAnswers = new HashMap<>(); // quizCode -> (playerName -> answers)

    @MessageMapping("/fetch/{quizCode}")
    @SendTo("/topic/quiz")
    public List<QuestionWebSocketDTO> fetchQuizQuestions(int quizCode) {
        return questionService.getAllQuestions(quizCode);
    }

//    @MessageMapping("/submit/{quizCode}/{playerName}")
//    public void submitAnswers(int quizCode, String playerName, List<String> answers) {
//        List<QuestionWebSocketDTO> questions = questionService.getAllQuestions(quizCode);
//
//        if (questions == null || answers.size() != questions.size()) {
//            messagingTemplate.convertAndSend("/topic/quiz/" + quizCode, "Error: Answer count mismatch.");
//            return;
//        }
//
//        int correctCount = 0;
//        for (int i = 0; i < questions.size(); i++) {
//            if (questions.get(i).getCorrectAnswer().equalsIgnoreCase(answers.get(i))) {
//                correctCount++;
//            }
//        }
//
//        // Store player results
//        playerAnswers.putIfAbsent(quizCode, new HashMap<>());
//        playerAnswers.get(quizCode).put(playerName, correctCount + "/" + questions.size());
//
//        // Send results to all players in the session
//        messagingTemplate.convertAndSend("/topic/quiz/" + quizCode, "Player " + playerName + " Score: " + correctCount + "/" + questions.size());
//    }
}
