package org.pvtmsgbot.resources;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;

public class CacheStore {
    private static final Cache<String, Object> cache = Caffeine.newBuilder()
        .maximumSize(1000)
        .build();

    private CacheStore() {}

    public static void put(String key, Object value) {
        cache.put(key, value);
    }

    public static Object get(String key) {
        return cache.getIfPresent(key);
    }

    public static String getString(String key) {
        Object value = get(key);
        return value instanceof String ? (String) value : null;
    }
}
