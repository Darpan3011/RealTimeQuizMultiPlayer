package com.darpan.realtimemultiplayerquiz.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LeaderboardDTO {
    private String playerName;
    private int score;
    private LocalDateTime timestamp;

    public LeaderboardDTO(String playerName, int score, LocalDateTime timestamp) {
        this.playerName = playerName;
        this.score = score;
        this.timestamp = timestamp;
    }
}
