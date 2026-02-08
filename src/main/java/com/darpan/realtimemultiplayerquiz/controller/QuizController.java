package com.darpan.realtimemultiplayerquiz.controller;

import com.darpan.realtimemultiplayerquiz.dto.CreateQuizDTO;
import com.darpan.realtimemultiplayerquiz.dto.QuizDTO;
import com.darpan.realtimemultiplayerquiz.dto.SuccessResponse;
import com.darpan.realtimemultiplayerquiz.model.Quiz;
import com.darpan.realtimemultiplayerquiz.service.QuizService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @PostMapping
    public ResponseEntity<SuccessResponse> addQuiz(@Valid @RequestBody CreateQuizDTO createQuizDTO) {
        Quiz quiz = new Quiz();
        quiz.setQuizCode(createQuizDTO.getQuizCode());
        quiz.setQuizName(createQuizDTO.getQuizName());
        quizService.saveQuiz(quiz);
        return new ResponseEntity<>(new SuccessResponse("Quiz created successfully"), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<QuizDTO>> getAllQuiz() {
        List<QuizDTO> all = quizService.getAllQuiz();
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizDTO> getQuizById(@PathVariable int id) {
        QuizDTO q = quizService.getQuizById(id);
        return new ResponseEntity<>(q, HttpStatus.OK);
    }

    @GetMapping("/code/{id}")
    public ResponseEntity<QuizDTO> getQuizByCode(@PathVariable int id) {
        QuizDTO q = quizService.getQuizByCode(id);
        return new ResponseEntity<>(q, HttpStatus.OK);
    }

    @GetMapping("/leaderboard/{quizCode}")
    public ResponseEntity<List<com.darpan.realtimemultiplayerquiz.dto.LeaderboardDTO>> getLeaderboard(
            @PathVariable int quizCode) {
        List<com.darpan.realtimemultiplayerquiz.dto.LeaderboardDTO> leaderboard = quizService.getLeaderboard(quizCode);
        return new ResponseEntity<>(leaderboard, HttpStatus.OK);
    }
}
