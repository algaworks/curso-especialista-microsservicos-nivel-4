package com.algaworks.algashop.product.catalog.infrastructure.storage.s3;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

@Component("awsS3")
@RequiredArgsConstructor
public class StorageProviderAwsS3HealthIndicator implements HealthIndicator {

    private final StorageProviderAwsS3Impl storageProviderAwsS3;

    @Override
    public @Nullable Health health() {
        if (storageProviderAwsS3.healthCheck()) {
            return Health.up().build();
        }
        return Health.status("DEGRADED").build();
    }
}
