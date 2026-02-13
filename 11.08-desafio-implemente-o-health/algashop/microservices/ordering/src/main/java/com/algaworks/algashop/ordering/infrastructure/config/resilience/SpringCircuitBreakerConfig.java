package com.algaworks.algashop.ordering.infrastructure.config.resilience;

import com.algaworks.algashop.ordering.infrastructure.adapters.in.web.exceptionhandler.BadGatewayException;
import com.algaworks.algashop.ordering.infrastructure.adapters.in.web.exceptionhandler.GatewayTimeoutException;
import org.springframework.cloud.circuitbreaker.retry.FrameworkRetryCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.retry.RetryPolicy;

import java.time.Duration;

@Configuration
public class SpringCircuitBreakerConfig {

    public static final String productCatalogCBId = "productCatalogCB";
    public static final String rapidexAPICBId = "rapidexAPICB";

    @Bean
    public Customizer<FrameworkRetryCircuitBreakerFactory> defaultCustomizer() {
        RetryPolicy retryPolicy = RetryPolicy.builder()
                .maxRetries(3)
                .multiplier(2)
                .delay(Duration.ofSeconds(3))
                .includes(GatewayTimeoutException.class, BadGatewayException.ServerErrorException.class)
                .build();
        
        return factory -> {
            factory.configure(builder -> builder
                    .retryPolicy(retryPolicy)
                    .openTimeout(Duration.ofSeconds(30))
                    .resetTimeout(Duration.ofSeconds(60))
                    .build(), productCatalogCBId
            );

            factory.configure(builder -> builder
                    .retryPolicy(retryPolicy)
                    .openTimeout(Duration.ofSeconds(30))
                    .resetTimeout(Duration.ofSeconds(60))
                    .build(), rapidexAPICBId
            );
        };
    }

}
