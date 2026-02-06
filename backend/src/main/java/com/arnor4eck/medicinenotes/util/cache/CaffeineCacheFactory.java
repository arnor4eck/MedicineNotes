package com.arnor4eck.medicinenotes.util.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class CaffeineCacheFactory implements CacheFactory<Cache<Object, Object>> {
    @Override
    public Cache<Object, Object> create(String name, int size, long minutesAfterCreating) {
        return Caffeine.newBuilder()
                .recordStats()
                .expireAfterWrite(minutesAfterCreating, TimeUnit.MINUTES)
                .maximumSize(size)
                .build();
    }
}
