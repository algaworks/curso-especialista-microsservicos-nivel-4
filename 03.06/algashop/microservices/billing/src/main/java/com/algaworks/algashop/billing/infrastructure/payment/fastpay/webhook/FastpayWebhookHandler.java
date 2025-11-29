package com.algaworks.algashop.billing.infrastructure.payment.fastpay.webhook;

import com.algaworks.algashop.billing.application.invoice.management.InvoiceManagementApplicationService;
import com.algaworks.algashop.billing.infrastructure.payment.fastpay.FastpayEnumConverter;
import com.algaworks.algashop.billing.infrastructure.payment.fastpay.FastpayPaymentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class FastpayWebhookHandler {

    private final InvoiceManagementApplicationService invoiceManagementApplicationService;

    public void process(FastpayPaymentWebhookEvent event) {
        log.info("Processing webhook event: {}", event);
        invoiceManagementApplicationService.updatePaymentStatus(
                UUID.fromString(event.getReferenceCode()),
                FastpayEnumConverter.convert(FastpayPaymentStatus.valueOf(event.getStatus()))
        );
    }
}
