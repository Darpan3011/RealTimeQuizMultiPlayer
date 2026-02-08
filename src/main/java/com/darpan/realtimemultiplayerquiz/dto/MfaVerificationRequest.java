package com.darpan.realtimemultiplayerquiz.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MfaVerificationRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String code;
}
