package com.algaworks.algashop.billing.infrastructure.creditcard.fastpay;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange(value = "/api/v1/credit-cards", accept = "application/json")
public interface FastpayCreditCardAPIClient {
    @PostExchange(contentType = "application/json")
    FastpayCreditCardResponse create(@RequestBody FastpayCreditCardInput input);

    @GetExchange("/{creditCardId}")
    FastpayCreditCardResponse findById(@PathVariable String creditCardId);

    @DeleteExchange("/{creditCardId}")
    void delete(@PathVariable String creditCardId);
}
