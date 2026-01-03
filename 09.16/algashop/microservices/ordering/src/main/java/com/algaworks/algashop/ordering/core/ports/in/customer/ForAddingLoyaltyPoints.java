package com.algaworks.algashop.ordering.core.ports.in.customer;

import java.util.UUID;

public interface ForAddingLoyaltyPoints {
    void addLoyaltyPoints(UUID rawCustomerId, String rawOrderId);
}
