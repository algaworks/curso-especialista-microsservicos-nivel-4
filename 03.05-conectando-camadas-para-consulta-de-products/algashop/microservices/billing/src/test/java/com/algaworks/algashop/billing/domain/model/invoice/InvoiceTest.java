package com.algaworks.algashop.billing.domain.model.invoice;

import com.algaworks.algashop.billing.domain.model.DomainException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

class InvoiceTest {

    @Test
    public void shouldMarkInvoiceAsPaidWhenUnpaid() {
        Invoice invoice = InvoiceTestDataBuilder.anInvoice().build();
        invoice.changePaymentSettings(PaymentMethod.GATEWAY_BALANCE, null);

        invoice.markAsPaid();

        Assertions.assertWith(invoice,
                i -> Assertions.assertThat(i.isPaid()).isTrue(),
                i -> Assertions.assertThat(i.getPaidAt()).isNotNull()
        );
    }
    
    @Test
    public void shouldCancelInvoiceWithReason() {
        Invoice invoice = InvoiceTestDataBuilder.anInvoice().build();
        String cancelReason = "Customer requested cancellation";

        invoice.cancel(cancelReason);

        Assertions.assertWith(invoice,
                i -> Assertions.assertThat(i.isCanceled()).isTrue(),
                i -> Assertions.assertThat(i.getCanceledAt()).isNotNull(),
                i -> Assertions.assertThat(i.getCancelReason()).isEqualTo(cancelReason)
        );
    }
    
    @Test
    public void shouldIssueInvoiceCorrectly() {
        String orderId = "123";
        UUID customerId = UUID.randomUUID();
        Payer payer = InvoiceTestDataBuilder.aPayer();
        Set<LineItem> items = new HashSet<>();
        items.add(InvoiceTestDataBuilder.aLineItem());
        items.add(InvoiceTestDataBuilder.aLineItemAlt());

        Invoice invoice = Invoice.issue(orderId, customerId, payer, items);

        BigDecimal expectedTotalAmount = invoice.getItems()
                .stream()
                .map(LineItem::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Assertions.assertWith(invoice,
                i -> Assertions.assertThat(i.getId()).isNotNull(),
                i -> Assertions.assertThat(i.getTotalAmount()).isEqualTo(expectedTotalAmount),
                i -> Assertions.assertThat(i.getStatus()).isEqualTo(InvoiceStatus.UNPAID)
        );
    }

    @Test
    public void shouldThrowExceptionWhenIssuingInvoiceWithEmptyItems() {
        Set<LineItem> emptyItems = new HashSet<>();
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Invoice.issue("01226N0693HDA23",
	                UUID.randomUUID(), 
		              InvoiceTestDataBuilder.aPayer(), 
		              emptyItems));
    }

    @Test
    public void shouldChangePaymentSettingsWhenUnpaid() {
        Invoice invoice = InvoiceTestDataBuilder.anInvoice().build();
        UUID creditCardId = UUID.randomUUID();
        invoice.changePaymentSettings(PaymentMethod.CREDIT_CARD, creditCardId);
        Assertions.assertWith(invoice,
                i -> Assertions.assertThat(i.getPaymentSettings()).isNotNull(),
                i -> Assertions.assertThat(i.getPaymentSettings().getMethod()).isEqualTo(PaymentMethod.CREDIT_CARD),
                i -> Assertions.assertThat(i.getPaymentSettings().getCreditCardId()).isEqualTo(creditCardId));
    }

    @Test
    public void shouldThrowExceptionWhenChangingPaymentSettingsToPaidInvoice() {
        Invoice invoice = InvoiceTestDataBuilder.anInvoice().status(InvoiceStatus.PAID).build();
        Assertions.assertThatExceptionOfType(DomainException.class)
                .isThrownBy(() -> invoice.changePaymentSettings(PaymentMethod.CREDIT_CARD, UUID.randomUUID()));
    }

    

    @Test
    public void shouldThrowExceptionWhenMarkingCanceledInvoiceAsPaid() {
        Invoice invoice = InvoiceTestDataBuilder.anInvoice().status(InvoiceStatus.CANCELED).build();

        Assertions.assertThatExceptionOfType(DomainException.class)
                .isThrownBy(invoice::markAsPaid);
    }

    @Test
    public void shouldThrowExceptionWhenCancelingAlreadyCanceledInvoice() {
        Invoice invoice = InvoiceTestDataBuilder.anInvoice().status(InvoiceStatus.CANCELED).build();

        Assertions.assertThatExceptionOfType(DomainException.class)
                .isThrownBy(() -> invoice.cancel("Another reason"));
    }

    @Test
    public void shouldAssignPaymentGatewayCodeWhenUnpaid() {
        Invoice invoice = InvoiceTestDataBuilder.anInvoice().paymentSettings(PaymentMethod.CREDIT_CARD, UUID.randomUUID()).build();
        String gatewayCode = "code-from-gateway";

        invoice.assignPaymentGatewayCode(gatewayCode);

        Assertions.assertThat(invoice.getPaymentSettings().getGatewayCode()).isEqualTo(gatewayCode);
    }

    @Test
    public void shouldThrowExceptionWhenAssigningGatewayCodeToPaidInvoice() {
        Invoice invoice = InvoiceTestDataBuilder.anInvoice().status(InvoiceStatus.PAID).build();
        Assertions.assertThatExceptionOfType(DomainException.class)
                .isThrownBy(() -> invoice.assignPaymentGatewayCode("some-code"));
    }

    @Test
    public void shouldThrowExceptionWhenTryingToModifyItemsSet() {
        Invoice invoice = InvoiceTestDataBuilder.anInvoice().build();
        Set<LineItem> items = invoice.getItems();

        Assertions.assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(items::clear);
    }
}