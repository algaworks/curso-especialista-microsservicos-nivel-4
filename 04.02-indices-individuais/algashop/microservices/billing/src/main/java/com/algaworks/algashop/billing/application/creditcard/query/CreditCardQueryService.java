package com.algaworks.algashop.billing.application.creditcard.query;

import java.util.List;
import java.util.UUID;

public interface CreditCardQueryService {
    CreditCardOutput findOne(UUID customerId, UUID creditCardId);
    List<CreditCardOutput> findByCustomer(UUID customerId);
}
