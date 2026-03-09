package com.arnor4eck.medicinenotes.config.rate_limiter;

import com.arnor4eck.medicinenotes.util.rate_limiter.RateLimiterCreationRequest;
import com.arnor4eck.medicinenotes.util.rate_limiter.Resilience4jRateLimiterFactory;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class RateLimitersInitializer {

    private final Resilience4jRateLimiterFactory factory;

    @PostConstruct
    public void init() {
        RateLimiterCreationRequest[] requests = new RateLimiterCreationRequest[]{
                new RateLimiterCreationRequest("authLimiter", 25, 60),
                new RateLimiterCreationRequest("registrationLimiter", 5, 60)
        };

        for(var req : requests)
            factory.create(req);
    }
}

