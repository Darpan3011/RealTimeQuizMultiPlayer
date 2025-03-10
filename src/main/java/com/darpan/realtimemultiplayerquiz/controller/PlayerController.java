package com.darpan.realtimemultiplayerquiz.controller;

import com.darpan.realtimemultiplayerquiz.dto.PlayerDTO;
import com.darpan.realtimemultiplayerquiz.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/player")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @PostMapping
    public void addPlayer(@RequestBody PlayerDTO playerDTO) {
        playerService.addPlayer(playerDTO);
    }

}
