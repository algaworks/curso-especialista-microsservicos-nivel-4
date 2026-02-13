package com.algaworks.algashop.billing.infrastructure.payment.fastpay;

import com.algaworks.algashop.billing.presentation.BadGatewayException;
import com.algaworks.algashop.billing.presentation.GatewayTimeoutException;
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

@Component
@Slf4j
public class ResilientFastpayPaymentAPIClient {

    private final FastpayPaymentAPIClient fastpayPaymentAPIClient;
    private final FrameworkRetryCircuitBreaker circuitBreaker;

    public ResilientFastpayPaymentAPIClient(
            CircuitBreakerFactory<FrameworkRetryConfig, FrameworkRetryConfigBuilder> circuitBreakerFactory,
            FastpayPaymentAPIClient fastpayPaymentAPIClient
    ) {
        this.fastpayPaymentAPIClient = fastpayPaymentAPIClient;
        this.circuitBreaker = (FrameworkRetryCircuitBreaker) circuitBreakerFactory.create("fastpayPaymentCB");
    }

    @ConcurrencyLimit(10)
    public FastpayPaymentModel capture(FastpayPaymentInput input) {
        log.info("Trying to capture payment on Fastpay");

        try {
            return circuitBreaker.run(() -> {
                try {
                    return doCapture(input);
                } catch (RestClientException e) {
                    throw new FastpayPaymentCaptureFailed(
                            "Fail to capture payment of reference code %s"
                                    .formatted(input.getReferenceCode()), e);
                }
            });
        } catch (NoFallbackAvailableException e) {
            throw unwrapException(e);
        }
    }

    @ConcurrencyLimit(10)
    public FastpayPaymentModel findById(String paymentId) {
        log.info("Trying to find payment {} on Fastpay", paymentId);

        try {
            return circuitBreaker.run(() -> doFindById(paymentId));
        } catch (NoFallbackAvailableException e) {
            throw unwrapException(e);
        }
    }

    @ConcurrencyLimit(10)
    public void refund(String paymentId) {
        log.info("Trying to refund payment {} on Fastpay", paymentId);

        try {
            circuitBreaker.run(() -> {
                doRefund(paymentId);
                return Void.TYPE;
            });
        } catch (NoFallbackAvailableException e) {
            throw unwrapException(e);
        }
    }

    @ConcurrencyLimit(10)
    public void cancel(String paymentId) {
        log.info("Trying to cancel payment {} on Fastpay", paymentId);

        try {
            circuitBreaker.run(() -> {
                doCancel(paymentId);
                return Void.TYPE;
            });
        } catch (NoFallbackAvailableException e) {
            throw unwrapException(e);
        }
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

    private FastpayPaymentModel doCapture(FastpayPaymentInput input) {
        try {
            return fastpayPaymentAPIClient.capture(input);
        } catch (RestClientException e) {
            throw translateException(e);
        }
    }

    private FastpayPaymentModel doFindById(String paymentId) {
        try {
            return fastpayPaymentAPIClient.findById(paymentId);
        } catch (RestClientException e) {
            throw translateException(e);
        }
    }

    private void doRefund(String paymentId) {
        try {
            fastpayPaymentAPIClient.refund(paymentId);
        } catch (RestClientException e) {
            throw translateException(e);
        }
    }

    private void doCancel(String paymentId) {
        try {
            fastpayPaymentAPIClient.cancel(paymentId);
        } catch (RestClientException e) {
            throw translateException(e);
        }
    }

    private RuntimeException translateException(RestClientException e) {
        if (e.getCause() instanceof SocketTimeoutException
                || e instanceof ResourceAccessException) {
            return new GatewayTimeoutException("Fastpay API Timeout", e);
        }

        if (e instanceof HttpClientErrorException) {
            return new BadGatewayException.ClientErrorException("Fastpay API Bad Gateway", e);
        }

        if (e instanceof HttpServerErrorException) {
            return new BadGatewayException.ServerErrorException("Fastpay API Bad Gateway", e);
        }

        return new BadGatewayException("Fastpay API Bad Gateway", e);
    }
}
