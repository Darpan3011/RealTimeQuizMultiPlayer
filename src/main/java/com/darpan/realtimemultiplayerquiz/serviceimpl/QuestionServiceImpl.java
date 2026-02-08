package com.darpan.realtimemultiplayerquiz.serviceimpl;

import com.darpan.realtimemultiplayerquiz.dao.QuestionRepository;
import com.darpan.realtimemultiplayerquiz.dao.QuizRepository;
import com.darpan.realtimemultiplayerquiz.dto.QuestionClientDTO;
import com.darpan.realtimemultiplayerquiz.dto.QuestionDTO;
import com.darpan.realtimemultiplayerquiz.dto.QuestionWebSocketDTO;
import com.darpan.realtimemultiplayerquiz.exception.QuizNotFoundException;
import com.darpan.realtimemultiplayerquiz.model.Question;
import com.darpan.realtimemultiplayerquiz.model.Quiz;
import com.darpan.realtimemultiplayerquiz.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final QuizRepository quizRepository;

    @Override
    @Transactional
    public void createQuestion(QuestionDTO questionDTO) {
        log.debug("Creating question for quiz ID: {}", questionDTO.getQuizId());
        Quiz quiz = quizRepository.findById(questionDTO.getQuizId())
                .orElseThrow(() -> new QuizNotFoundException(questionDTO.getQuizId()));

        Question question = new Question();
        question.setQuestionTitle(questionDTO.getQuestionTitle());
        question.setCorrectAnswerIndex(questionDTO.getCorrectAnswerIndex());
        question.setOptions(questionDTO.getOptions());
        question.setQuiz(quiz);

        questionRepository.save(question);
        log.info("Question created successfully with ID: {} for quiz: {}", question.getId(), quiz.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionWebSocketDTO> getAllQuestions(int quizId) {
        log.debug("Fetching all questions with answers for quiz ID: {}", quizId);
        List<Question> questions = questionRepository.findByQuizId(quizId);
        log.info("Retrieved {} questions for quiz ID: {}", questions.size(), quizId);
        return questions.stream().map(x -> new QuestionWebSocketDTO(
                x.getQuestionTitle(),
                x.getOptions(),
                x.getId(),
                x.getCorrectAnswerIndex())).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionClientDTO> getQuestionsForClients(int quizId) {
        log.debug("Fetching client-safe questions (without answers) for quiz ID: {}", quizId);
        List<Question> questions = questionRepository.findByQuizId(quizId);
        log.info("Retrieved {} client-safe questions for quiz ID: {}", questions.size(), quizId);
        return questions.stream().map(x -> new QuestionClientDTO(
                x.getId(),
                x.getQuestionTitle(),
                x.getOptions())).toList();
    }
}
