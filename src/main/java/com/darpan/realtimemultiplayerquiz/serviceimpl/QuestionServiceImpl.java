package com.darpan.realtimemultiplayerquiz.serviceimpl;

import com.darpan.realtimemultiplayerquiz.dao.QuestionRepository;
import com.darpan.realtimemultiplayerquiz.dao.QuizRepository;
import com.darpan.realtimemultiplayerquiz.dto.QuestionDTO;
import com.darpan.realtimemultiplayerquiz.dto.QuestionWebSocketDTO;
import com.darpan.realtimemultiplayerquiz.model.Question;
import com.darpan.realtimemultiplayerquiz.model.Quiz;
import com.darpan.realtimemultiplayerquiz.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final QuizRepository quizRepository;

    @Override
    public void createQuestion(QuestionDTO questionDTO) {
        Quiz quiz = quizRepository.findById(questionDTO.getQuizId())
                .orElseThrow(() -> new RuntimeException("Quiz not found with ID: " + questionDTO.getQuizId()));

        Question question = new Question();
        question.setQuestionTitle(questionDTO.getQuestionTitle());
        question.setCorrectAnswer(questionDTO.getCorrectAnswer());
        question.setOptions(questionDTO.getOptions());
        question.setQuiz(quiz);

        questionRepository.save(question);
    }

    @Override
    public List<QuestionWebSocketDTO> getAllQuestions(int quizId) {
        List<Question> questions = questionRepository.findByQuizId(quizId);
        return questions.stream().map(x -> new QuestionWebSocketDTO(
                x.getQuestionTitle(),
                x.getOptions(),
                x.getId(),
                x.getCorrectAnswer())).toList();
    }
}
