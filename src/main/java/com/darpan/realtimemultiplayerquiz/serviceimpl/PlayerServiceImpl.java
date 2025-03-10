package com.darpan.realtimemultiplayerquiz.serviceimpl;

import com.darpan.realtimemultiplayerquiz.dao.PlayerDAO;
import com.darpan.realtimemultiplayerquiz.dto.PlayerDTO;
import com.darpan.realtimemultiplayerquiz.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private PlayerDAO playerDAO;

    @Override
    public void addPlayer(PlayerDTO player) {
        playerDAO.addPlayer(player);
    }
}
