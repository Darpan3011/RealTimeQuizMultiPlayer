package com.darpan.realtimemultiplayerquiz.serviceimpl;

import com.darpan.realtimemultiplayerquiz.dao.PlayerRepository;
import com.darpan.realtimemultiplayerquiz.dao.QuizRepository;
import com.darpan.realtimemultiplayerquiz.dto.PlayerDTO;
import com.darpan.realtimemultiplayerquiz.model.Player;
import com.darpan.realtimemultiplayerquiz.model.Quiz;
import com.darpan.realtimemultiplayerquiz.service.PlayerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final QuizRepository quizRepository;

    public PlayerServiceImpl(PlayerRepository playerRepository, QuizRepository quizRepository) {
        this.playerRepository = playerRepository;
        this.quizRepository = quizRepository;
    }

    @Override
    public void addPlayer(PlayerDTO playerDTO) {
        Quiz quiz = quizRepository.findByQuizCode(playerDTO.getQuizCode())
                .orElseThrow(() -> new RuntimeException("Quiz not found with code: " + playerDTO.getQuizCode()));

        Player player = new Player();
        player.setName(playerDTO.getName());
        player.setQuizzes(List.of(quiz));

        playerRepository.save(player);
    }
}
