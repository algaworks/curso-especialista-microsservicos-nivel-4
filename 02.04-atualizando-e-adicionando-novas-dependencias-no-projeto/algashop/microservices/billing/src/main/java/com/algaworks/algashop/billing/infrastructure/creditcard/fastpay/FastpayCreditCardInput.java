package com.algaworks.algashop.billing.infrastructure.creditcard.fastpay;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FastpayCreditCardInput {
    private String tokenizedCard;
    private String customerCode;
}
