package com.arnor4eck.medicinenotes.service.code_verifier;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/** Класс для хранения количества попыток на ввод кода в Redis
 * */
@Component
public class AttemptsRedisStorage extends AbstractRedisStorage<Integer>{
    public AttemptsRedisStorage(RedisTemplate<String, Object> redisTemplate,
                                @Value("${redis.timeout.attempts}") int timeout) {
        super(redisTemplate, "attempts:", timeout, o -> Integer.parseInt(o.toString()));
    }
}
