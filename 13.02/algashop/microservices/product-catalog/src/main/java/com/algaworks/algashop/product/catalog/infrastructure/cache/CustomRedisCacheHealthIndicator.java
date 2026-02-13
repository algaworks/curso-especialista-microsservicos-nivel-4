package com.algaworks.algashop.product.catalog.infrastructure.cache;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

@Component("cache")
@RequiredArgsConstructor
@ConditionalOnProperty(name = "spring.cache.type", havingValue = "redis")
public class CustomRedisCacheHealthIndicator implements HealthIndicator {

    private final RedisConnectionFactory redisConnectionFactory;

    @Override
    public @Nullable Health health() {
        try {
            redisConnectionFactory.getConnection().ping();
            return Health.up().build();
        } catch (Exception e) {
            return Health.status("DEGRADED")
                    .withDetail("error", e.getMessage())
                    .withException(e)
                    .build();
        }
    }
}
