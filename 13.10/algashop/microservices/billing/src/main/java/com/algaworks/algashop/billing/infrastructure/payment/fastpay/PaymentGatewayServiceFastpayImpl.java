package com.algaworks.algashop.billing.infrastructure.payment.fastpay;

import com.algaworks.algashop.billing.domain.model.creditcard.CreditCard;
import com.algaworks.algashop.billing.domain.model.creditcard.CreditCardNotFoundException;
import com.algaworks.algashop.billing.domain.model.creditcard.CreditCardRepository;
import com.algaworks.algashop.billing.domain.model.invoice.Address;
import com.algaworks.algashop.billing.domain.model.invoice.Payer;
import com.algaworks.algashop.billing.domain.model.invoice.payment.Payment;
import com.algaworks.algashop.billing.domain.model.invoice.payment.PaymentGatewayService;
import com.algaworks.algashop.billing.domain.model.invoice.payment.PaymentRequest;
import com.algaworks.algashop.billing.infrastructure.payment.AlgaShopPaymentProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@ConditionalOnProperty(name = "algashop.integrations.payment.provider", havingValue = "FASTPAY")
@RequiredArgsConstructor
@Slf4j
public class PaymentGatewayServiceFastpayImpl implements PaymentGatewayService {

    private final ResilientFastpayPaymentAPIClient fastpayPaymentAPIClient;
    private final CreditCardRepository creditCardRepository;

    private final AlgaShopPaymentProperties algaShopPaymentProperties;

    @Override
    public Payment capture(PaymentRequest request) {
        FastpayPaymentInput input = convertToInput(request);
        FastpayPaymentModel response = fastpayPaymentAPIClient.capture(input);

        return convertToPayment(response);
    }

    @Override
    public Payment findByCode(String gatewayCode) {
        FastpayPaymentModel response = fastpayPaymentAPIClient.findById(gatewayCode);
        return convertToPayment(response);
    }

    private FastpayPaymentInput convertToInput(PaymentRequest request) {
        Payer payer = request.getPayer();
        Address address = payer.getAddress();

        var builder = FastpayPaymentInput.builder()
                .totalAmount(request.getAmount())
                .referenceCode(request.getInvoiceId().toString())
                .fullName(payer.getFullName())
                .document(payer.getDocument())
                .phone(payer.getPhone())
                .zipCode(address.getZipCode())
                .addressLine1(address.getStreet() + ", " + address.getNumber())
                .addressLine2(address.getComplement())
                .replyToUrl(algaShopPaymentProperties.getFastpay().getWebhookUrl());

        switch (request.getMethod()) {
            case CREDIT_CARD -> {
                builder.method(FastpayPaymentMethod.CREDIT.name());
                CreditCard creditCard = creditCardRepository.findById(request.getCreditCardId())
                        .orElseThrow(() -> new CreditCardNotFoundException());
                builder.creditCardId(creditCard.getGatewayCode());
            }
            case GATEWAY_BALANCE -> builder.method(FastpayPaymentMethod.GATEWAY_BALANCE.name());
        }

        return builder.build();
    }

    private Payment convertToPayment(FastpayPaymentModel response) {
        var builder = Payment.builder()
                .gatewayCode(response.getId())
                .invoiceId(UUID.fromString(response.getReferenceCode()));

        FastpayPaymentMethod fastpayPaymentMethod;

        try {
            fastpayPaymentMethod = FastpayPaymentMethod.valueOf(response.getMethod());
        } catch (Exception e) {
            throw new IllegalArgumentException("Unknown payment method: " + response.getMethod());
        }

        FastpayPaymentStatus fastpayPaymentStatus;
        try {
            fastpayPaymentStatus = FastpayPaymentStatus.valueOf(response.getStatus());
        } catch (Exception e) {
            throw new IllegalArgumentException("Unknown payment status: " + response.getStatus());
        }

        builder.method(FastpayEnumConverter.convert(fastpayPaymentMethod));
        builder.status(FastpayEnumConverter.convert(fastpayPaymentStatus));

        return builder.build();
    }
}
