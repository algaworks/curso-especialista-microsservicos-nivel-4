package com.algaworks.algashop.billing.infrastructure.payment.fastpay;

import com.algaworks.algashop.billing.presentation.BadGatewayException;
import com.algaworks.algashop.billing.presentation.GatewayTimeoutException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.cloud.circuitbreaker.retry.FrameworkRetryCircuitBreakerFactory;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class ResilientFastpayPaymentAPIClientTest {

    private FastpayPaymentAPIClient delegate;
    private ResilientFastpayPaymentAPIClient client;

    @BeforeEach
    void setUp() {
        delegate = Mockito.mock(FastpayPaymentAPIClient.class);
        FrameworkRetryCircuitBreakerFactory factory = new FrameworkRetryCircuitBreakerFactory();
        client = new ResilientFastpayPaymentAPIClient(factory, delegate);
    }

    @Test
    void findById_whenResourceAccessException_shouldThrowGatewayTimeout() {
        when(delegate.findById(anyString()))
                .thenThrow(new org.springframework.web.client.ResourceAccessException("timeout"));

        assertThatThrownBy(() -> client.findById("pay_123"))
                .isInstanceOf(GatewayTimeoutException.class)
                .hasMessageContaining("Fastpay API Timeout");
    }

    @Test
    void capture_whenHttpClientErrorException_shouldThrowBadGateway() {
        when(delegate.capture(any(FastpayPaymentInput.class)))
                .thenThrow(org.springframework.web.client.HttpClientErrorException.create(
                        org.springframework.http.HttpStatus.BAD_REQUEST,
                        "bad request",
                        org.springframework.http.HttpHeaders.EMPTY,
                        null,
                        null
                ));

        assertThatThrownBy(() -> client.capture(FastpayPaymentInput.builder().build()))
                .isInstanceOf(BadGatewayException.class)
                .hasMessageContaining("Fastpay API Bad Gateway");
    }
}
