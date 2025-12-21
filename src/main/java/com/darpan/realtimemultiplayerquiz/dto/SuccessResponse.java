package com.darpan.realtimemultiplayerquiz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Generic response DTO for successful operations
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SuccessResponse {
    private String message;
    private Object data;

    public SuccessResponse(String message) {
        this.message = message;
    }
}
