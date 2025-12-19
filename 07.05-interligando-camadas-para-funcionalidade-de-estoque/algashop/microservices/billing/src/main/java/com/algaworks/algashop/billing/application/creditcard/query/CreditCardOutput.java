package com.algaworks.algashop.billing.application.creditcard.query;

import lombok.Data;

import java.util.UUID;

@Data
public class CreditCardOutput {
    private UUID id;
    private String lastNumbers;
    private Integer expMonth;
    private Integer expYear;
    private String brand;
}
