package com.arnor4eck.medicinenotes.util.cache;

import org.springframework.cache.Cache;

public interface CacheFactory<T> {
    T create(String name, int size, long minutesAfterCreating);
}
