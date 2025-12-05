package com.algaworks.algashop.billing.infrastructure.creditcard.fastpay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FastpayTokenizationInput {
    private String number;
    private String cvv;
    private String holderName;
    private String holderDocument;
    private Integer expMonth;
    private Integer expYear;
}
