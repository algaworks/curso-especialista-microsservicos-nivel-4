package com.algaworks.algashop.ordering.core.ports.in.customer;

import java.util.UUID;

public interface ForConfirmCustomerRegistration {
    void confirm(UUID customerId);
}
