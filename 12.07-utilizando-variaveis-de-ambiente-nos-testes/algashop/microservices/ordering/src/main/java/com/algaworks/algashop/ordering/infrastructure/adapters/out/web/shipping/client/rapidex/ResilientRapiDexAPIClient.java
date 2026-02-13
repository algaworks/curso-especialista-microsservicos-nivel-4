package com.algaworks.algashop.ordering.infrastructure.adapters.out.web.shipping.client.rapidex;

import com.algaworks.algashop.ordering.infrastructure.adapters.in.web.exceptionhandler.BadGatewayException;
import com.algaworks.algashop.ordering.infrastructure.adapters.in.web.exceptionhandler.GatewayTimeoutException;
import com.algaworks.algashop.ordering.infrastructure.config.resilience.SpringCircuitBreakerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.circuitbreaker.retry.FrameworkRetryCircuitBreaker;
import org.springframework.cloud.circuitbreaker.retry.FrameworkRetryConfig;
import org.springframework.cloud.circuitbreaker.retry.FrameworkRetryConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.NoFallbackAvailableException;
import org.springframework.core.retry.RetryException;
import org.springframework.resilience.annotation.ConcurrencyLimit;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

import java.net.SocketTimeoutException;

import static com.algaworks.algashop.ordering.infrastructure.config.resilience.SpringCircuitBreakerConfig.rapidexAPICBId;

@Component
@Slf4j
public class ResilientRapiDexAPIClient {

    private final RapiDexAPIClient rapiDexAPIClient;
    private final FrameworkRetryCircuitBreaker circuitBreaker;

    public ResilientRapiDexAPIClient(CircuitBreakerFactory<FrameworkRetryConfig,
                                        FrameworkRetryConfigBuilder> circuitBreakerFactory,
                                     RapiDexAPIClient rapiDexAPIClient) {
        this.rapiDexAPIClient = rapiDexAPIClient;
        this.circuitBreaker = (FrameworkRetryCircuitBreaker) circuitBreakerFactory.create(rapidexAPICBId);
    }

    @ConcurrencyLimit(15)
    public DeliveryCostResponse calculate(DeliveryCostRequest request) {
        log.info("RapidexAPI CircuitBreaker state is {}", circuitBreaker.getCircuitBreakerPolicy().getState());
        try {
            DeliveryCostResponse response = circuitBreaker.run(
                    () -> doCalculate(request),
                    ex -> doInternalFallback(request, ex)
            );
            if (response == null) {
                throw new BadGatewayException.ClientErrorException("Invalid zip code provided");
            }
            return response;
        } catch (NoFallbackAvailableException e) {
            throw unwrapException(e);
        }
    }

    private DeliveryCostResponse doInternalFallback(DeliveryCostRequest request, Throwable exThrowable) {
        log.warn("Rapidex API call failed for request {}", request, exThrowable);
        return new DeliveryCostResponse("20.0", 10L);
    }

    private RuntimeException unwrapException(NoFallbackAvailableException e) {
        if (e.getCause() instanceof RetryException re) {
            if (re.getCause() instanceof GatewayTimeoutException gte) {
                return gte;
            }
            if (re.getCause() instanceof BadGatewayException bge) {
                return bge;
            }
        }
        return e;
    }

    private DeliveryCostResponse doCalculate(DeliveryCostRequest request) {
        try {
            return rapiDexAPIClient.calculate(request);
        }
        catch (HttpClientErrorException e) {
            if (!(e instanceof HttpClientErrorException.NotFound)) {
                log.warn("Client Error when loading delivery cost {}", request, e);
            }
            return null;
        } catch (RestClientException e) {
            throw translateException(e);
        }
    }

    private RuntimeException translateException(RestClientException e) {
        if (e.getCause() instanceof SocketTimeoutException
                || e instanceof ResourceAccessException) {
            return new GatewayTimeoutException("Rapidex API Timeout", e);
        }

        if (e instanceof HttpClientErrorException) {
            return new BadGatewayException.ClientErrorException("Rapidex API Bad Gateway", e);
        }

        if (e instanceof HttpServerErrorException) {
            return new BadGatewayException.ServerErrorException("Rapidex API Bad Gateway", e);
        }

        return new BadGatewayException("Rapidex API Bad Gateway", e);
    }

}
