package com.arnor4eck.medicinenotes.test_utils;

import com.arnor4eck.medicinenotes.util.controller_advice.ExceptionResponseFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;


import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestConfiguration
public class TestConfig {
    @Bean
    @Primary
    public ExceptionResponseFactory exceptionResponseFactory() {
        ExceptionResponseFactory factory = mock(ExceptionResponseFactory.class);

        when(factory.create(anyString(), any(), any())).thenCallRealMethod();
        when(factory.create(anyList(), any(), any())).thenCallRealMethod();

        return factory;
    }
}
