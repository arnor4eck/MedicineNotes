package com.arnor4eck.medicinenotes.config.cache;

import com.arnor4eck.medicinenotes.util.cache.CaffeineCacheFactory;
import lombok.AllArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
@AllArgsConstructor
public class CacheConfig {

    private final CaffeineCacheFactory caffeineCacheFactory;

    private final CacheProperties properties;

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();

        caffeineCacheManager.setCacheNames(properties.getNames());

        properties.getNames().forEach(name ->
                caffeineCacheManager.registerCustomCache(name,
                        caffeineCacheFactory.create(name, 100, 20))
        );

        return caffeineCacheManager;
    }
}
