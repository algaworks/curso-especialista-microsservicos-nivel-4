package com.algaworks.algashop.billing.domain.model.creditcard;

import com.algaworks.algashop.billing.domain.model.DomainEntityNotFoundException;

public class CreditCardNotFoundException extends DomainEntityNotFoundException {
    public CreditCardNotFoundException() {
    }

    public CreditCardNotFoundException(Throwable cause) {
        super(cause);
    }

    public CreditCardNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreditCardNotFoundException(String message) {
        super(message);
    }
}
