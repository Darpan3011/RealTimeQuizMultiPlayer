package com.darpan.realtimemultiplayerquiz.service;

import com.darpan.realtimemultiplayerquiz.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.email.from}")
    private String fromEmail;

    @Value("${app.email.app-name}")
    private String appName;

    @Value("${spring.mail.username}")
    private String smtpUsername;

    public void sendVerificationEmail(User user, String token) {
        String subject = "Verify Your Email - " + appName;
        String verificationUrl = "http://localhost:4200/verify-email?token=" + token;

        String htmlContent = buildEmailTemplate(
                "Welcome to " + appName + "!",
                "Hi " + user.getFirstName() + ",",
                "Thank you for registering! Please verify your email address by clicking the button below:",
                "Verify Email",
                verificationUrl,
                "This link will expire in 24 hours.");

        log.info("Verification URL: {}", verificationUrl); // For testing purposes
        sendHtmlEmail(user.getEmail(), subject, htmlContent);
    }

    public void sendMfaCode(User user, String code) {
        String subject = "Your Verification Code - " + appName;

        String htmlContent = buildEmailTemplate(
                "Verification Code",
                "Hi " + user.getFirstName() + ",",
                "Your verification code is:",
                code,
                null,
                "This code will expire in 5 minutes. If you didn't request this code, please ignore this email.");

        sendHtmlEmail(user.getEmail(), subject, htmlContent);
    }

    public void sendPasswordResetEmail(User user, String token) {
        String subject = "Reset Your Password - " + appName;
        String resetUrl = "http://localhost:4200/reset-password?token=" + token;

        String htmlContent = buildEmailTemplate(
                "Password Reset Request",
                "Hi " + user.getFirstName() + ",",
                "We received a request to reset your password. Click the button below to proceed:",
                "Reset Password",
                resetUrl,
                "This link will expire in 1 hour. If you didn't request this, please ignore this email.");

        sendHtmlEmail(user.getEmail(), subject, htmlContent);
    }

    private void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Email sent successfully to: {}", to);
        } catch (MessagingException | MailException e) {
            log.error("Failed to send email to: {}. Error: {}", to, e.getMessage());
            // Gracefully handle email failures - don't throw exception
            // In production, you might want to queue for retry
        }
    }

    private String buildEmailTemplate(String title, String greeting, String message,
            String buttonText, String buttonUrl, String footer) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html><head><meta charset='UTF-8'><style>");
        html.append("body{font-family:Arial,sans-serif;background-color:#f4f4f4;margin:0;padding:0}");
        html.append(
                ".container{max-width:600px;margin:40px auto;background:#fff;border-radius:8px;overflow:hidden;box-shadow:0 2px 10px rgba(0,0,0,0.1)}");
        html.append(
                ".header{background:linear-gradient(135deg,#8b5cf6,#6366f1);color:#fff;padding:30px;text-align:center}");
        html.append(".content{padding:30px;color:#333}");
        html.append(
                ".code-box{background:#f0f0f0;border:2px dashed #8b5cf6;border-radius:8px;padding:20px;text-align:center;font-size:32px;font-weight:bold;color:#8b5cf6;letter-spacing:8px;margin:20px 0}");
        html.append(
                ".button{display:inline-block;background:#8b5cf6;color:#fff;padding:12px 30px;text-decoration:none;border-radius:6px;margin:20px 0;font-weight:bold}");
        html.append(
                ".footer{background:#f9f9f9;padding:20px;text-align:center;color:#666;font-size:12px;border-top:1px solid #eee}");
        html.append("</style></head><body>");
        html.append("<div class='container'>");
        html.append("<div class='header'><h1>").append(title).append("</h1></div>");
        html.append("<div class='content'>");
        html.append("<p>").append(greeting).append("</p>");
        html.append("<p>").append(message).append("</p>");

        if (buttonUrl != null) {
            html.append("<div style='text-align:center'>");
            html.append("<a href='").append(buttonUrl).append("' class='button'>").append(buttonText).append("</a>");
            html.append("</div>");
        } else {
            // This is for MFA code
            html.append("<div class='code-box'>").append(buttonText).append("</div>");
        }

        html.append("<p style='margin-top:30px;color:#666;font-size:14px'>").append(footer).append("</p>");
        html.append("</div>");
        html.append("<div class='footer'>");
        html.append("<p>&copy; 2026 ").append(appName).append(". All rights reserved.</p>");
        html.append("<p>Sent from ").append(smtpUsername).append("</p>");
        html.append("</div>");
        html.append("</div></body></html>");

        return html.toString();
    }
}
