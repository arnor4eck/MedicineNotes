package com.arnor4eck.medicinenotes.util.rate_limiter;

public record RateLimiterCreationRequest(String name, int limit, int refreshPeriod) {
}
