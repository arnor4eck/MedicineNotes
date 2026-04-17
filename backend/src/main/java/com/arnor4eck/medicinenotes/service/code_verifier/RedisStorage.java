package com.arnor4eck.medicinenotes.service.code_verifier;

import java.util.Optional;

public interface RedisStorage<V> {
    Optional<V> get(String key);
    void put(String key, V value);
    void remove(String key);
}
