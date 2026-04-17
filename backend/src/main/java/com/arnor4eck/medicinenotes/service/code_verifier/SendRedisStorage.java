package com.arnor4eck.medicinenotes.service.code_verifier;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/** Класс для хранения количества отправленных кодов подтверждения в Redis
 * */
@Component
public class SendRedisStorage extends AbstractRedisStorage<Integer> {
    public SendRedisStorage(RedisTemplate<String, Object> redisTemplate,
                                @Value("${redis.timeout.send}") int timeout) {
        super(redisTemplate, "send:", timeout, o -> Integer.parseInt(o.toString()));
    }
}
