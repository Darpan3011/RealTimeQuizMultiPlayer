package com.darpan.realtimemultiplayerquiz.controller;

import com.darpan.realtimemultiplayerquiz.dto.QuizDTO;
import com.darpan.realtimemultiplayerquiz.model.Quiz;
import com.darpan.realtimemultiplayerquiz.service.QuizService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quiz")
public class QuizController {

    private QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping
    public ResponseEntity addQuiz(@RequestBody Quiz quiz){
        quizService.saveQuiz(quiz);
        return new ResponseEntity<>("Quiz added successfully", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity getAllQuiz(){
        List<QuizDTO> all =  quizService.getAllQuiz();
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity getQuizById(@PathVariable int id){
        QuizDTO q =  quizService.getQuizById(id);
        return new ResponseEntity<>(q, HttpStatus.OK);
    }
}
