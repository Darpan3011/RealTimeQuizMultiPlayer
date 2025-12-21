package com.darpan.realtimemultiplayerquiz.serviceimpl;

import com.darpan.realtimemultiplayerquiz.dao.PlayerRepository;
import com.darpan.realtimemultiplayerquiz.dao.QuizRepository;
import com.darpan.realtimemultiplayerquiz.dto.PlayerDTO;
import com.darpan.realtimemultiplayerquiz.exception.QuizNotFoundException;
import com.darpan.realtimemultiplayerquiz.model.Player;
import com.darpan.realtimemultiplayerquiz.model.Quiz;
import com.darpan.realtimemultiplayerquiz.service.PlayerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final QuizRepository quizRepository;

    @Override
    @Transactional
    public void addPlayer(PlayerDTO playerDTO) {
        log.debug("Adding player: {} to quiz code: {}", playerDTO.getName(), playerDTO.getQuizCode());
        Quiz quiz = quizRepository.findByQuizCode(playerDTO.getQuizCode())
                .orElseThrow(() -> new QuizNotFoundException("code", playerDTO.getQuizCode()));

        Player player = new Player();
        player.setName(playerDTO.getName());
        player.setQuizzes(List.of(quiz));

        playerRepository.save(player);
        log.info("Player added successfully: {} with ID: {}", player.getName(), player.getId());
    }
}
