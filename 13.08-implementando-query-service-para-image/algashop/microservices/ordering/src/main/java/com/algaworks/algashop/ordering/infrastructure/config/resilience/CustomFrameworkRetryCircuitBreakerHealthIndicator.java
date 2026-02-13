package com.algaworks.algashop.ordering.infrastructure.config.resilience;

import org.jspecify.annotations.Nullable;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.cloud.circuitbreaker.retry.CircuitBreakerRetryPolicy;
import org.springframework.cloud.circuitbreaker.retry.FrameworkRetryCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.algaworks.algashop.ordering.infrastructure.config.resilience.SpringCircuitBreakerConfig.productCatalogCBId;
import static com.algaworks.algashop.ordering.infrastructure.config.resilience.SpringCircuitBreakerConfig.rapidexAPICBId;

@Component("circuitbreakers")
public class CustomFrameworkRetryCircuitBreakerHealthIndicator implements HealthIndicator {

    private final List<FrameworkRetryCircuitBreaker> circuitBreakers = new ArrayList<>();

    public CustomFrameworkRetryCircuitBreakerHealthIndicator(CircuitBreakerFactory circuitBreakerFactory) {
        circuitBreakers.add((FrameworkRetryCircuitBreaker) circuitBreakerFactory.create(productCatalogCBId));
        circuitBreakers.add((FrameworkRetryCircuitBreaker) circuitBreakerFactory.create(rapidexAPICBId));
    }

    @Override
    public @Nullable Health health() {
        Map<String, Object> indicatorDetails = new HashMap<>();
        String indicatorStatus = "UP";
        Throwable lastException = null;

        for (FrameworkRetryCircuitBreaker circuitBreaker : circuitBreakers) {
            var policy = circuitBreaker.getConfig().getCircuitBreakerRetryPolicy();
            var state = policy.getState();

            Map<String, Object> cbDetails = new HashMap<>();
            cbDetails.put("state", state.name());

            if (state == CircuitBreakerRetryPolicy.State.OPEN) {
                indicatorStatus = "DEGRADED";
                if (policy.getLastException() != null
                        && policy.getLastException().getCause() != null) {
                    lastException = policy.getLastException().getCause();
                    cbDetails.put("lastException", lastException.getMessage());
                } else {
                    cbDetails.put("lastException", null);
                }
            }

            indicatorDetails.put(circuitBreaker.getId(), cbDetails);
        }

        Health.Builder builder = Health.status(indicatorStatus).withDetails(indicatorDetails);

        if (indicatorStatus.equals("DEGRADED") && lastException != null) {
            builder.withException(lastException);
        }

        return builder.build();
    }
}
