package com.darpan.realtimemultiplayerquiz.dao;

import com.darpan.realtimemultiplayerquiz.model.RefreshToken;
import com.darpan.realtimemultiplayerquiz.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    List<RefreshToken> findByUser(User user);

    List<RefreshToken> findByExpiryDateBeforeAndRevokedFalse(LocalDateTime currentTime);

    void deleteByUser(User user);
}
