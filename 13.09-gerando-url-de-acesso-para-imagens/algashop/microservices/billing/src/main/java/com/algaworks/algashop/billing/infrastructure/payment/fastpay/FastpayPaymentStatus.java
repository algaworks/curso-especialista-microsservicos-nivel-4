package com.algaworks.algashop.billing.infrastructure.payment.fastpay;

public enum FastpayPaymentStatus {
    PENDING,
    PROCESSING,
    FAILED,
    PAID,
    CANCELED,
    REFUNDED
}
