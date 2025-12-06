package com.algaworks.algashop.ordering.core.ports.in.order;

public interface ForManagingOrders {
    void cancel(String rawOrderId);
    void markAsPaid(String rawOrderId);
    void markAsReady(String rawOrderId);
}
