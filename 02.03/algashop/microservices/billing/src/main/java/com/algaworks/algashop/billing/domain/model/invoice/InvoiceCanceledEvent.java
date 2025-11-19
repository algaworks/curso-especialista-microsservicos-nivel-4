package com.algaworks.algashop.billing.domain.model.invoice;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class InvoiceCanceledEvent {
    private UUID invoiceId;
    private UUID customerId;
    private String orderId;
    private OffsetDateTime canceledAt;
}
