package com.algaworks.algashop.ordering.infrastructure.adapters.out.web.product.client.http;

import com.algaworks.algashop.ordering.infrastructure.adapters.in.web.exceptionhandler.BadGatewayException;
import com.algaworks.algashop.ordering.infrastructure.adapters.in.web.exceptionhandler.GatewayTimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.NoFallbackAvailableException;
import org.springframework.core.retry.RetryException;
import org.springframework.resilience.annotation.ConcurrencyLimit;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

import java.net.SocketTimeoutException;
import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
public class ResilientProductCatalogAPIClient {

    private final ProductCatalogAPIClient productCatalogAPIClient;
    private final CircuitBreaker circuitBreaker;

    public ResilientProductCatalogAPIClient(CircuitBreakerFactory circuitBreakerFactory,
                                            ProductCatalogAPIClient productCatalogAPIClient) {
        this.productCatalogAPIClient = productCatalogAPIClient;
        this.circuitBreaker = circuitBreakerFactory.create("productCatalogCB");
    }

    @Cacheable(cacheNames = "algashop:product-catalog-api:v1", key = "#productId")
    @ConcurrencyLimit(10)
    public Optional<ProductResponse> getById(UUID productId) {
        log.info("Trying to load product {}", productId);
        try {
            return circuitBreaker.run(()->loadProduct(productId));
        } catch (NoFallbackAvailableException e) {
            if (e.getCause() instanceof RetryException re) {
                if (re.getCause() instanceof GatewayTimeoutException gte) {
                    throw gte;
                }
                if (re.getCause() instanceof BadGatewayException bge) {
                    throw bge;
                }
            }
            throw e;
        }
    }

    private Optional<ProductResponse> loadProduct(UUID productId) {
        log.info("Loading product {}", productId);
        try {
            return Optional.ofNullable(productCatalogAPIClient.getById(productId));
        } catch (HttpClientErrorException e) {
            if (!(e instanceof HttpClientErrorException.NotFound)) {
                log.error("Client HTTP error when loading product {}", productId, e);
            }
            return Optional.empty();
        } catch (RestClientException e) {
            throw translateException(e);
        }
    }

    private RuntimeException translateException(RestClientException e) {
        if (e.getCause() instanceof SocketTimeoutException
            || e instanceof ResourceAccessException) {
            return new GatewayTimeoutException("Product Catalog API Timeout", e);
        }

        if (e instanceof HttpClientErrorException) {
            return new BadGatewayException.ClientErrorException("Product Catalog API Bad Gateway", e);
        }

        if (e instanceof HttpServerErrorException) {
            return new BadGatewayException.ServerErrorException("Product Catalog API Bad Gateway", e);
        }

        return new BadGatewayException("Product Catalog API Bad Gateway", e);
    }

}
