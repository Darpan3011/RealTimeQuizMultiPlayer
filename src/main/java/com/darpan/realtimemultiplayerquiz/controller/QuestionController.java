package com.darpan.realtimemultiplayerquiz.controller;

import com.darpan.realtimemultiplayerquiz.dto.QuestionClientDTO;
import com.darpan.realtimemultiplayerquiz.dto.QuestionDTO;
import com.darpan.realtimemultiplayerquiz.dto.SuccessResponse;
import com.darpan.realtimemultiplayerquiz.model.Question;
import com.darpan.realtimemultiplayerquiz.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/question")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping
    public ResponseEntity<SuccessResponse> createQuestion(@Valid @RequestBody QuestionDTO question) {
        questionService.createQuestion(question);
        return new ResponseEntity<>(new SuccessResponse("Question created successfully"), HttpStatus.CREATED);
    }

    @GetMapping("/quiz/{id}")
    public ResponseEntity<List<QuestionClientDTO>> getQuizById(@PathVariable int id) {
        List<QuestionClientDTO> questions = questionService.getQuestionsForClients(id);
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

}
