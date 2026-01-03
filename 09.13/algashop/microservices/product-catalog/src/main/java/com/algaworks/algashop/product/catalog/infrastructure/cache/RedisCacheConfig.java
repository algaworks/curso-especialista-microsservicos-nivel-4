package com.algaworks.algashop.product.catalog.infrastructure.cache;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.cache.autoconfigure.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;

@Configuration
@EnableCaching
@ConditionalOnProperty(name = "spring.cache.type", havingValue = "redis")
public class RedisCacheConfig {

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        var defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .computePrefixWith(c -> c + ":");
        return (builder) -> builder.cacheDefaults(defaultCacheConfig)
                .withCacheConfiguration("algashop:products:v1", defaultCacheConfig.disableCachingNullValues());
    }

}
