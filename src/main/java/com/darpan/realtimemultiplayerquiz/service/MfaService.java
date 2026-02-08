package com.darpan.realtimemultiplayerquiz.service;

import com.darpan.realtimemultiplayerquiz.dao.EmailVerificationTokenRepository;
import com.darpan.realtimemultiplayerquiz.model.EmailVerificationToken;
import com.darpan.realtimemultiplayerquiz.model.TokenType;
import com.darpan.realtimemultiplayerquiz.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MfaService {

    private final EmailVerificationTokenRepository tokenRepository;
    private final EmailService emailService;

    @Value("${app.security.mfa.enabled}")
    private boolean mfaEnabled;

    @Value("${app.security.mfa.code-expiration}")
    private long codeExpirationMs;

    @Value("${app.security.mfa.code-length}")
    private int codeLength;

    private static final SecureRandom random = new SecureRandom();

    public boolean isMfaEnabled() {
        return mfaEnabled;
    }

    public String generateMfaCode() {
        int min = (int) Math.pow(10, codeLength - 1);
        int max = (int) Math.pow(10, codeLength) - 1;
        int code = random.nextInt(max - min + 1) + min;
        return String.valueOf(code);
    }

    public void sendMfaCode(User user) {
        if (!mfaEnabled) {
            return;
        }

        String code = generateMfaCode();
        LocalDateTime expiryDate = LocalDateTime.now().plusSeconds(codeExpirationMs / 1000);

        EmailVerificationToken token = new EmailVerificationToken();
        token.setToken(code);
        token.setUser(user);
        token.setTokenType(TokenType.MFA_CODE);
        token.setExpiryDate(expiryDate);
        token.setVerified(false);

        tokenRepository.save(token);
        emailService.sendMfaCode(user, code);
    }

    public boolean verifyMfaCode(User user, String code) {
        if (!mfaEnabled) {
            return true; // If MFA is disabled, always return true
        }

        Optional<EmailVerificationToken> tokenOpt = tokenRepository
                .findByUserAndTokenTypeAndVerifiedFalseAndExpiryDateAfter(
                        user, TokenType.MFA_CODE, LocalDateTime.now());

        if (tokenOpt.isEmpty()) {
            return false;
        }

        EmailVerificationToken token = tokenOpt.get();
        if (token.getToken().equals(code)) {
            token.setVerified(true);
            tokenRepository.save(token);
            return true;
        }

        return false;
    }

    public void cleanupExpiredTokens() {
        tokenRepository.deleteAll(
                tokenRepository.findByExpiryDateBeforeAndVerifiedFalse(LocalDateTime.now()));
    }
}
