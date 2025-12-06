package com.algaworks.algashop.ordering.core.domain.model.order;

import com.algaworks.algashop.ordering.core.domain.model.customer.CustomerId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class OrderCancelTest {

    @Test
    void givenEmptyOrder_whenCancel_shouldAllow() {
        Order order = Order.draft(new CustomerId());

        order.cancel();

        Assertions.assertWith(order,
                (i) -> Assertions.assertThat(i.status()).isEqualTo(OrderStatus.CANCELED),
                (i) -> Assertions.assertThat(i.isCanceled()).isTrue(),
                (i) -> Assertions.assertThat(i.canceledAt()).isNotNull()
        );
    }

    @Test
    void givenFilledOrder_whenCancel_shouldAllow() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.DRAFT).build();

        order.cancel();

        Assertions.assertWith(order,
                (i) -> Assertions.assertThat(i.status()).isEqualTo(OrderStatus.CANCELED),
                (i) -> Assertions.assertThat(i.isCanceled()).isTrue(),
                (i) -> Assertions.assertThat(i.canceledAt()).isNotNull()
        );
    }

    @Test
    void givenCanceledOrder_whenCancelAgain_shouldThrowException() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.CANCELED).build();

        Assertions.assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(order::cancel);

        Assertions.assertWith(order,
                (i) -> Assertions.assertThat(i.status()).isEqualTo(OrderStatus.CANCELED),
                (i) -> Assertions.assertThat(i.isCanceled()).isTrue(),
                (i) -> Assertions.assertThat(i.canceledAt()).isNotNull()
        );
    }

}
