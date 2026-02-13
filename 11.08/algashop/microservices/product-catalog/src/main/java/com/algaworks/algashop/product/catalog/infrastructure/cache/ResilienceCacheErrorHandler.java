package com.algaworks.algashop.product.catalog.infrastructure.cache;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@ConditionalOnProperty(name = "spring.cache.type", havingValue = "redis")
public class ResilienceCacheErrorHandler implements CacheErrorHandler {

    @Override
    public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
        String method = "GET";
        logWarn(exception, cache, key, method);
    }

    @Override
    public void handleCachePutError(RuntimeException exception, Cache cache, Object key, @Nullable Object value) {
        String method = "PUT";
        if (exception instanceof SerializationException) {
            logError(exception, cache, key, method);
        } else {
            logWarn(exception, cache, key, method);
        }
    }

    @Override
    public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
        String method = "EVICT";
        logWarn(exception, cache, key, method);
    }

    @Override
    public void handleCacheClearError(RuntimeException exception, Cache cache) {
        String method = "CLEAR";
        logWarn(exception, cache, "", method);
    }

    private void logWarn(RuntimeException exception, Cache cache, Object key, String method) {
        log.warn("Cache {} error | cache='{}' | key = '{}' | cause='{}'",
                method,
                cache.getName(),
                key,
                exception.getClass().getSimpleName()
        );
    }

    private void logError(RuntimeException exception, Cache cache, Object key, String method) {
        log.error("Cache {} error | cache='{}' | key = '{}' | cause='{}'",
                method,
                cache.getName(),
                key,
                exception.getClass().getSimpleName(),
                exception
        );
    }
}
