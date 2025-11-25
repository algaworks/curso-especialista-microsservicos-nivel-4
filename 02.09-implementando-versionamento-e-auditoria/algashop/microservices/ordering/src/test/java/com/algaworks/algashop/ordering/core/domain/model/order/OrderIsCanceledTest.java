package com.algaworks.algashop.ordering.core.domain.model.order;

import com.algaworks.algashop.ordering.core.domain.model.commons.Quantity;
import com.algaworks.algashop.ordering.core.domain.model.customer.CustomerId;
import com.algaworks.algashop.ordering.core.domain.model.product.ProductTestDataBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class OrderIsCanceledTest {

    @Test
    void givenCanceledOrder_whenIsCanceled_shouldReturnTrue() {
        Order order = Order.draft(new CustomerId());
        Assertions.assertThat(order.isCanceled()).isFalse();
        order.cancel();
        Assertions.assertThat(order.isCanceled()).isTrue();
    }

    @Test
    void givenNonCanceledOrder_whenIsCanceled_shouldReturnFalse() {
        Order order = Order.draft(new CustomerId());
        order.addItem(ProductTestDataBuilder.aProduct().build(), new Quantity(2));

        Assertions.assertThat(order.isCanceled()).isFalse();
    }

    @Test
    void givenOrderInAnyOtherStatus_whenIsCanceled_shouldReturnFalse() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PAID).build();
        Assertions.assertThat(order.isCanceled()).isFalse();
    }
}
