package com.arnor4eck.medicinenotes.service.code_verifier;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

// Скелетная реализация
abstract class AbstractRedisStorage<V> implements RedisStorage<V> {

    protected final RedisTemplate<String, Object> redisTemplate;
    protected final String PREFIX;
    protected final int TIME_OUT;
    protected final Function<Object, ? extends V> mapper;

    public AbstractRedisStorage(RedisTemplate<String, Object> redisTemplate,
                            String prefix, int timeout,
                            Function<Object, ? extends V> mapper) {
        this.redisTemplate = Objects.requireNonNull(redisTemplate);
        this.PREFIX = Objects.requireNonNull(prefix);
        this.mapper = Objects.requireNonNull(mapper);

        this.TIME_OUT = timeout;
    }

    @Override
    public Optional<V> get(String key) {
        Object value = redisTemplate.opsForValue().get(PREFIX + key);

        return Optional.ofNullable(value)
                .map(mapper);
    }

    @Override
    public void put(String key, V value) {
        redisTemplate.opsForValue().set(PREFIX + key, Objects.requireNonNull(value),
                TIME_OUT, TimeUnit.MINUTES);
    }

    @Override
    public void remove(String key) {
        redisTemplate.delete(PREFIX + key);
    }
}
