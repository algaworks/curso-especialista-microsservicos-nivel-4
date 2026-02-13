package com.algaworks.algashop.billing.infrastructure.creditcard.fastpay;

import lombok.Data;

@Data
public class FastpayCreditCardResponse {
    private String id;
    private String lastNumbers;
    private Integer expMonth;
    private Integer expYear;
    private String brand;
}
