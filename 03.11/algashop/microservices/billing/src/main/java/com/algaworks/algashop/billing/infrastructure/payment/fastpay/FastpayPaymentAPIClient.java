package com.algaworks.algashop.billing.infrastructure.payment.fastpay;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;

@HttpExchange(value = "/api/v1/payments", accept = MediaType.APPLICATION_JSON_VALUE)
public interface FastpayPaymentAPIClient {
    @PostExchange(contentType = MediaType.APPLICATION_JSON_VALUE)
    FastpayPaymentModel capture(@RequestBody FastpayPaymentInput input);

    @GetExchange("/{paymentId}")
    FastpayPaymentModel findById(@PathVariable String paymentId);

    @PutExchange("/{paymentId}/refund")
    void refund(@PathVariable String paymentId);

    @PutExchange("/{paymentId}/cancel")
    void cancel(@PathVariable String paymentId);

}
