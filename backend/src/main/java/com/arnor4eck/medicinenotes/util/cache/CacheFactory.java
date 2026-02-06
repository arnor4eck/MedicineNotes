package com.arnor4eck.medicinenotes.util.cache;

public interface CacheFactory<T> {
    T create(String name, int size, long minutesAfterCreating);
}
