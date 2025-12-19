package com.algaworks.algashop.billing.application.invoice.query;

import com.algaworks.algashop.billing.domain.model.invoice.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentSettingsOutput {
    private UUID id;
    private UUID creditCardId;
    private PaymentMethod method;
}
