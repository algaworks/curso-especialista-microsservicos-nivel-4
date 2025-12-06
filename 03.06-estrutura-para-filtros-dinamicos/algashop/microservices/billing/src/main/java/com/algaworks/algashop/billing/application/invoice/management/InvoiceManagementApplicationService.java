package com.algaworks.algashop.billing.application.invoice.management;

import com.algaworks.algashop.billing.domain.model.creditcard.CreditCardNotFoundException;
import com.algaworks.algashop.billing.domain.model.creditcard.CreditCardRepository;
import com.algaworks.algashop.billing.domain.model.invoice.*;
import com.algaworks.algashop.billing.domain.model.invoice.payment.Payment;
import com.algaworks.algashop.billing.domain.model.invoice.payment.PaymentGatewayService;
import com.algaworks.algashop.billing.domain.model.invoice.payment.PaymentRequest;
import com.algaworks.algashop.billing.domain.model.invoice.payment.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvoiceManagementApplicationService {

    private final PaymentGatewayService paymentGatewayService;
    private final InvoicingService invoicingService;
    private final InvoiceRepository invoiceRepository;
    private final CreditCardRepository creditCardRepository;

    @Transactional
    public UUID generate(GenerateInvoiceInput input) {
        PaymentSettingsInput paymentSettings = input.getPaymentSettings();
        if (paymentSettings.getMethod().equals(PaymentMethod.CREDIT_CARD)) {
            verifyCreditCard(input);
        }

        Payer payer = convertToPayer(input.getPayer());
        Set<LineItem> items = convertToLineItems(input.getItems());

        Invoice invoice = invoicingService.issue(input.getOrderId(), input.getCustomerId(), payer, items);
        invoice.changePaymentSettings(paymentSettings.getMethod(), paymentSettings.getCreditCardId());

        invoiceRepository.saveAndFlush(invoice);

        return invoice.getId();
    }

    private void verifyCreditCard(GenerateInvoiceInput input) {
        UUID creditCardId = input.getPaymentSettings().getCreditCardId();
        UUID customerId = input.getCustomerId();
        if (!creditCardRepository.existsByIdAndCustomerId(creditCardId, customerId)) {
            throw new CreditCardNotFoundException(String.format("Credit card %s not found exception", creditCardId));
        }
    }

    @Transactional
    public void processPayment(UUID invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(() -> new InvoiceNotFoundException());
        PaymentRequest paymentRequest = toPaymentRequest(invoice);

        Payment payment;
        try {
            payment = paymentGatewayService.capture(paymentRequest);
        } catch (Exception e) {
            String errorMessage = "Payment capture failed";
            log.error(errorMessage, e);
            invoice.cancel(errorMessage);
            invoiceRepository.saveAndFlush(invoice);
            return;
        }

        invoicingService.assignPayment(invoice, payment);
        invoiceRepository.saveAndFlush(invoice);
    }

    @Transactional
    public void updatePaymentStatus(UUID invoiceId, PaymentStatus paymentStatus) {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(() -> new InvoiceNotFoundException());
        invoice.updatePaymentStatus(paymentStatus);
        invoiceRepository.saveAndFlush(invoice);
    }

    private PaymentRequest toPaymentRequest(Invoice invoice) {
        return PaymentRequest.builder()
                .amount(invoice.getTotalAmount())
                .method(invoice.getPaymentSettings().getMethod())
                .creditCardId(invoice.getPaymentSettings().getCreditCardId())
                .payer(invoice.getPayer())
                .invoiceId(invoice.getId())
                .build();
    }

    private Set<LineItem> convertToLineItems(List<LineItemInput> itemsInput) {
        Set<LineItem> lineItems = new LinkedHashSet<>();
        int itemNumber = 1;
        for (LineItemInput itemInput : itemsInput) {
            lineItems.add(LineItem.builder()
                            .number(itemNumber)
                            .name(itemInput.getName())
                            .amount(itemInput.getAmount())
                    .build());
            itemNumber++;
        }
        return lineItems;
    }

    private Payer convertToPayer(PayerData payerData) {
        AddressData addressData = payerData.getAddress();

        return Payer.builder()
                .fullName(payerData.getFullName())
                .email(payerData.getEmail())
                .document(payerData.getDocument())
                .phone(payerData.getPhone())
                .address(Address.builder()
                        .city(addressData.getCity())
                        .state(addressData.getState())
                        .neighborhood(addressData.getNeighborhood())
                        .complement(addressData.getComplement())
                        .zipCode(addressData.getZipCode())
                        .street(addressData.getStreet())
                        .number(addressData.getNumber())
                        .build())
                .build();
    }

}
