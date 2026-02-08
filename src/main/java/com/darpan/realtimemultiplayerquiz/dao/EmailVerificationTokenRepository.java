package com.darpan.realtimemultiplayerquiz.dao;

import com.darpan.realtimemultiplayerquiz.model.EmailVerificationToken;
import com.darpan.realtimemultiplayerquiz.model.TokenType;
import com.darpan.realtimemultiplayerquiz.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {
    Optional<EmailVerificationToken> findByToken(String token);

    Optional<EmailVerificationToken> findByUserAndTokenTypeAndVerifiedFalseAndExpiryDateAfter(
            User user, TokenType tokenType, LocalDateTime currentTime);

    List<EmailVerificationToken> findByExpiryDateBeforeAndVerifiedFalse(LocalDateTime currentTime);
}
