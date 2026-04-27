package com.arnor4eck.medicinenotes.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Configuration
public class RedisConfig {

    @Bean
    public RedisStandaloneConfiguration redisStandaloneConfiguration(@Value("${spring.data.redis.database}") int database,
                                                                     @Value("${spring.data.redis.host}") String host,
                                                                     @Value("${spring.data.redis.port}") int port,
                                                                     @Value("${spring.data.redis.password}") String password) {
        log.info("Настройки редиса");
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();

        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setDatabase(database);
        redisStandaloneConfiguration.setPassword(password);
        log.info("db: {}, host: {}, port: {}, password: {}", database, host, port, password);

        return redisStandaloneConfiguration;
    }

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory(RedisStandaloneConfiguration redisStandaloneConfiguration){
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory,
                                                       ObjectMapper objectMapper) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(lettuceConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        GenericJacksonJsonRedisSerializer serializer = new GenericJacksonJsonRedisSerializer(objectMapper);

        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        log.info("Настройка редиса окончена");
        return template;
    }
}

