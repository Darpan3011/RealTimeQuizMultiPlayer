package com.darpan.realtimemultiplayerquiz.service;

import com.darpan.realtimemultiplayerquiz.dao.EmailVerificationTokenRepository;
import com.darpan.realtimemultiplayerquiz.dao.RefreshTokenRepository;
import com.darpan.realtimemultiplayerquiz.dao.UserRepository;
import com.darpan.realtimemultiplayerquiz.dto.*;
import com.darpan.realtimemultiplayerquiz.model.*;
import com.darpan.realtimemultiplayerquiz.security.CustomUserDetails;
import com.darpan.realtimemultiplayerquiz.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final EmailVerificationTokenRepository emailTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final MfaService mfaService;
    private final RateLimitService rateLimitService;

    @Transactional
    public MessageResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRole(Role.USER);
        user.setMfaEnabled(false);
        user.setEmailVerified(false);
        user.setAccountLocked(false);

        userRepository.save(user);

        // Send verification email
        String token = UUID.randomUUID().toString();
        EmailVerificationToken verificationToken = new EmailVerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setTokenType(TokenType.REGISTRATION);
        verificationToken.setExpiryDate(LocalDateTime.now().plusHours(24));
        verificationToken.setVerified(false);

        emailTokenRepository.save(verificationToken);
        emailService.sendVerificationEmail(user, token);

        return new MessageResponse("Registration successful! Please check your email to verify your account.");
    }

    @Transactional
    public MessageResponse verifyEmail(String token) {
        EmailVerificationToken verificationToken = emailTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid verification token"));

        if (verificationToken.getVerified()) {
            throw new RuntimeException("Email already verified");
        }

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Verification token expired");
        }

        User user = verificationToken.getUser();
        user.setEmailVerified(true);
        userRepository.save(user);

        verificationToken.setVerified(true);
        emailTokenRepository.save(verificationToken);

        return new MessageResponse("Email verified successfully! You can now login.");
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        // Rate limiting check
        if (!rateLimitService.allowLoginAttempt(request.getEmail())) {
            throw new RuntimeException("Too many login attempts. Please try again later.");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!user.getEmailVerified()) {
            throw new RuntimeException("Please verify your email before logging in");
        }

        if (user.getAccountLocked()) {
            throw new RuntimeException("Account is locked. Please contact support.");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Reset failed login attempts on successful authentication
            user.setFailedLoginAttempts(0);
            userRepository.save(user);
            rateLimitService.resetLoginAttempts(request.getEmail());

            // Check if MFA is required
            if (mfaService.isMfaEnabled() && user.getMfaEnabled()) {
                mfaService.sendMfaCode(user);
                return new LoginResponse(null, null, true, "MFA code sent to your email");
            }

            // Generate tokens
            String accessToken = tokenProvider.generateToken(authentication);
            String refreshToken = createRefreshToken(user);

            return new LoginResponse(accessToken, refreshToken, false, "Login successful");

        } catch (Exception e) {
            // Increment failed login attempts
            user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
            if (user.getFailedLoginAttempts() >= 5) {
                user.setAccountLocked(true);
            }
            userRepository.save(user);
            throw new RuntimeException("Invalid credentials");
        }
    }

    @Transactional
    public LoginResponse verifyMfa(MfaVerificationRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!mfaService.verifyMfaCode(user, request.getCode())) {
            throw new RuntimeException("Invalid or expired MFA code");
        }

        // Generate tokens
        CustomUserDetails userDetails = CustomUserDetails.build(user);
        String accessToken = tokenProvider.generateTokenFromEmail(user.getEmail(), user.getRole().name());
        String refreshToken = createRefreshToken(user);

        return new LoginResponse(accessToken, refreshToken, false, "Login successful");
    }

    @Transactional
    public TokenRefreshResponse refreshToken(TokenRefreshRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (refreshToken.getRevoked()) {
            throw new RuntimeException("Refresh token has been revoked");
        }

        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh token expired");
        }

        User user = refreshToken.getUser();
        String newAccessToken = tokenProvider.generateTokenFromEmail(user.getEmail(), user.getRole().name());

        return new TokenRefreshResponse(newAccessToken);
    }

    @Transactional
    public MessageResponse logout(String refreshTokenString) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenString)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);

        return new MessageResponse("Logged out successfully");
    }

    @Transactional
    public MessageResponse enableMfa(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setMfaEnabled(true);
        userRepository.save(user);

        return new MessageResponse("MFA enabled successfully");
    }

    @Transactional
    public MessageResponse disableMfa(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setMfaEnabled(false);
        userRepository.save(user);

        return new MessageResponse("MFA disabled successfully");
    }

    private String createRefreshToken(User user) {
        String tokenString = UUID.randomUUID().toString();
        LocalDateTime expiryDate = LocalDateTime.now()
                .plusSeconds(tokenProvider.getRefreshTokenExpirationMs() / 1000);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(tokenString);
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(expiryDate);
        refreshToken.setRevoked(false);

        refreshTokenRepository.save(refreshToken);
        return tokenString;
    }
}
