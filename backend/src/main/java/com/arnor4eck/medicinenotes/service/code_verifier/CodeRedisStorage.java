package com.arnor4eck.medicinenotes.service.code_verifier;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/** Класс для хранения отправленного кода подтверждения в Redis
 * */
@Component
public class CodeRedisStorage extends AbstractRedisStorage<String>{
    public CodeRedisStorage(RedisTemplate<String, Object> redisTemplate,
                            @Value("${redis.timeout.code}") int timeout) {
        super(redisTemplate, "code:", timeout, Object::toString);
    }
}
