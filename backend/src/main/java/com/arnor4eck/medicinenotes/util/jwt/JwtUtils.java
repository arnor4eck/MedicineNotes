package com.arnor4eck.medicinenotes.util.jwt;

public interface JwtUtils <T> {
    String generateToken(T object);
    boolean validate(String token);
}
