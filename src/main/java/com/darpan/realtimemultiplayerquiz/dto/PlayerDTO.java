package com.darpan.realtimemultiplayerquiz.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PlayerDTO {

    private int quizCode;
    private String name;

    public PlayerDTO(int quizCode, String name) {
        this.quizCode = quizCode;
        this.name = name;
    }

}
