package com.algaworks.algashop.billing.domain.model.invoice;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class InvoiceTestDataBuilder {

    private String orderId = "01226N0693HDA";
    private UUID customerId = UUID.randomUUID();
    private Payer payer = aPayer();
    private PaymentMethod paymentMethod;
    private UUID creditCardId;
    private Set<LineItem> items = new HashSet<>();
    private InvoiceStatus status = InvoiceStatus.UNPAID;
    private String gatewayCode;
    private String cancelReason = "Test cancel reason";

    private InvoiceTestDataBuilder() {
        this.items.add(aLineItem());
    }

    public static InvoiceTestDataBuilder anInvoice() {
        return new InvoiceTestDataBuilder();
    }

    public Invoice build() {
        Invoice invoice = Invoice.issue(orderId, customerId, payer, items);

        if (paymentMethod != null) {
            invoice.changePaymentSettings(paymentMethod, creditCardId);
        }

        if (gatewayCode != null) {
            invoice.assignPaymentGatewayCode(gatewayCode);
        }

        switch (this.status) {
            case PAID -> invoice.markAsPaid();
            case CANCELED -> invoice.cancel(cancelReason);
        }

        return invoice;
    }

    public InvoiceTestDataBuilder items(Set<LineItem> items) {
        this.items = items;
        return this;
    }

    public InvoiceTestDataBuilder items(LineItem... items) {
        this.items = Set.of(items);
        return this;
    }

    public InvoiceTestDataBuilder status(InvoiceStatus status) {
        this.status = status;
        return this;
    }

    public InvoiceTestDataBuilder paymentSettings(PaymentMethod method, UUID creditCardId) {
        this.paymentMethod = method;
        this.creditCardId = creditCardId;
        return this;
    }

    public InvoiceTestDataBuilder gatewayCode(String gatewayCode) {
        this.gatewayCode = gatewayCode;
        return this;
    }

    public InvoiceTestDataBuilder orderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public InvoiceTestDataBuilder payer(Payer payer) {
        this.payer = payer;
        return this;
    }

    public InvoiceTestDataBuilder customerId(UUID customerId) {
        this.customerId = customerId;
        return this;
    }

    public InvoiceTestDataBuilder cancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
        return this;
    }

    public static LineItem aLineItem() {
        return LineItem.builder()
                .number(1)
                .name("Product 1")
                .amount(new BigDecimal("200.00"))
                .build();
    }

    public static LineItem aLineItemAlt() {
        return LineItem.builder()
                .number(2)
                .name("Product 2")
                .amount(new BigDecimal("150.00"))
                .build();
    }

    public static Payer aPayer() {
        return Payer.builder()
                .fullName("John Doe")
                .document("111.222.333-44")
                .phone("11-99999-8888")
                .email("john.doe@email.com")
                .address(Address.builder()
                        .street("Street Name")
                        .number("123")
                        .neighborhood("Neighborhood")
                        .city("City")
                        .state("State")
                        .zipCode("12345-678")
                        .build())
                .build();
    }
}