package com.algaworks.algashop.billing.infrastructure.payment.fastpay;

import com.algaworks.algashop.billing.domain.model.invoice.PaymentMethod;
import com.algaworks.algashop.billing.domain.model.invoice.payment.PaymentStatus;

public class FastpayEnumConverter {

    public static PaymentMethod convert(FastpayPaymentMethod method) {
        return switch (method) {
            case CREDIT -> PaymentMethod.CREDIT_CARD;
            case GATEWAY_BALANCE -> PaymentMethod.GATEWAY_BALANCE;
        };
    }

    public static PaymentStatus convert(FastpayPaymentStatus status) {
        return switch (status) {
            case PENDING -> PaymentStatus.PENDING;
            case PROCESSING -> PaymentStatus.PROCESSING;
            case FAILED -> PaymentStatus.FAILED;
            case PAID -> PaymentStatus.PAID;
            case CANCELED -> PaymentStatus.FAILED;
            case REFUNDED -> PaymentStatus.REFUNDED;
        };
    }

}
