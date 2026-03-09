package com.arnor4eck.medicinenotes.util.rate_limiter;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@AllArgsConstructor
public class Resilience4jRateLimiterFactory{

    private final RateLimiterRegistry rateLimiterRegistry;

    public RateLimiter create(RateLimiterCreationRequest request) {
        return rateLimiterRegistry.rateLimiter(request.name(),
                RateLimiterConfig.custom()
                        .limitForPeriod(request.limit()) // запросов в единицу времени
                        .timeoutDuration(Duration.ZERO) // время в течение которого поток ждет разрешения (если лимита нет - сразу отказываем)
                        .limitRefreshPeriod(Duration.ofSeconds(request.refreshPeriod())) // период за который ограничивается число вызовов
                        .build());
    }
}
