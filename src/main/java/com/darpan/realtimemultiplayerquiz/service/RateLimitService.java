package com.darpan.realtimemultiplayerquiz.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class RateLimitService {

    @Value("${app.security.rate-limit.login-attempts}")
    private int maxLoginAttempts;

    @Value("${app.security.rate-limit.login-window-minutes}")
    private int loginWindowMinutes;

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    public boolean allowLoginAttempt(String identifier) {
        Bucket bucket = buckets.computeIfAbsent(identifier, k -> createBucket());
        return bucket.tryConsume(1);
    }

    private Bucket createBucket() {
        Bandwidth limit = Bandwidth.classic(
                maxLoginAttempts,
                Refill.intervally(maxLoginAttempts, Duration.ofMinutes(loginWindowMinutes)));
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    public void resetLoginAttempts(String identifier) {
        buckets.remove(identifier);
        log.info("Reset login attempts for: {}", identifier);
    }

    public void cleanupOldBuckets() {
        // In a production environment, you'd want to implement
        // a more sophisticated cleanup mechanism
        if (buckets.size() > 10000) {
            buckets.clear();
            log.info("Cleared rate limit buckets due to size threshold");
        }
    }
}
