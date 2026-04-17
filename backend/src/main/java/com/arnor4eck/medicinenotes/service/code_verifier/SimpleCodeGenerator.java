package com.arnor4eck.medicinenotes.service.code_verifier;

import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class SimpleCodeGenerator implements CodeGenerator{
    @Override
    public String generateCode() {
        return String.valueOf(
                ThreadLocalRandom.current()
                        .nextInt(9999, 100000));
    }
}
