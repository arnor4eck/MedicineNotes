package com.arnor4eck.medicinenotes.service.code_verifier;

import com.arnor4eck.medicinenotes.util.PreRegistration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

/** Класс для хранения отправленного кода подтверждения в Redis
 * */
@Component
public class CodeRedisStorage extends AbstractRedisStorage<PreRegistration>{
    public CodeRedisStorage(RedisTemplate<String, Object> redisTemplate,
                            @Value("${redis.timeout.code}") int timeout) {
        super(redisTemplate, "code:", timeout, o -> {
            @SuppressWarnings("unckecked") // При десериализации объекта возвращается LinkedHashMap - приведение корректно
            LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) o;
            return new PreRegistration(map.get("email"), map.get("username"), map.get("password"), map.get("code"));
        });
    }
}
