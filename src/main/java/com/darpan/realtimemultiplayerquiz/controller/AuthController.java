package com.darpan.realtimemultiplayerquiz.controller;

import com.darpan.realtimemultiplayerquiz.dto.*;
import com.darpan.realtimemultiplayerquiz.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<MessageResponse> verifyEmail(@RequestParam String token) {
        return ResponseEntity.ok(authService.verifyEmail(token));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/verify-mfa")
    public ResponseEntity<LoginResponse> verifyMfa(@Valid @RequestBody MfaVerificationRequest request) {
        return ResponseEntity.ok(authService.verifyMfa(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenRefreshResponse> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logout(@Valid @RequestBody TokenRefreshRequest request) {
        return ResponseEntity.ok(authService.logout(request.getRefreshToken()));
    }

    @PutMapping("/mfa/enable")
    public ResponseEntity<MessageResponse> enableMfa(@RequestParam String email) {
        return ResponseEntity.ok(authService.enableMfa(email));
    }

    @PutMapping("/mfa/disable")
    public ResponseEntity<MessageResponse> disableMfa(@RequestParam String email) {
        return ResponseEntity.ok(authService.disableMfa(email));
    }
}
