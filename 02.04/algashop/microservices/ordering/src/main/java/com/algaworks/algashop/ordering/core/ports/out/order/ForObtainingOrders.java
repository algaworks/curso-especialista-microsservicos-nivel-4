package com.algaworks.algashop.ordering.core.ports.out.order;

import com.algaworks.algashop.ordering.core.ports.in.order.OrderFilter;
import org.springframework.data.domain.Page;

public interface ForObtainingOrders {
    OrderDetailOutput findById(String id);
    Page<OrderSummaryOutput> filter(OrderFilter filter);
}
