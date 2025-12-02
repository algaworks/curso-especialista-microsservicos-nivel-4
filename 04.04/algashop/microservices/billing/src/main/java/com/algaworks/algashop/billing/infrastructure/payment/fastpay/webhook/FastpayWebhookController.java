package com.algaworks.algashop.billing.infrastructure.payment.fastpay.webhook;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/webhooks/fastpay")
@RequiredArgsConstructor
public class FastpayWebhookController {

    private final FastpayWebhookHandler fastpayWebhookHandler;

    @PostMapping
    public void receive(@Valid @RequestBody FastpayPaymentWebhookEvent event) {
        fastpayWebhookHandler.process(event);
    }

}
