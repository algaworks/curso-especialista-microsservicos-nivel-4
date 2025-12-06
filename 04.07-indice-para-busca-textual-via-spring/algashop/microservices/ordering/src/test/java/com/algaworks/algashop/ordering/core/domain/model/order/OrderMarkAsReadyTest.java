package com.algaworks.algashop.ordering.core.domain.model.order;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderMarkAsReadyTest {

    @Test
    void givenPaidOrder_whenMarkAsReady_shouldUpdateStatusAndTimestamp() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PAID).build();

        order.markAsReady();

        Assertions.assertWith(order,
                (o) -> Assertions.assertThat(o.status()).isEqualTo(OrderStatus.READY),
                (o) -> Assertions.assertThat(o.readyAt()).isNotNull()
        );
    }

    @Test
    void givenDraftOrder_whenMarkAsReady_shouldThrowExceptionAndNotChangeState() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.DRAFT).build();

        Assertions.assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(order::markAsReady);

        Assertions.assertWith(order,
                (o) -> Assertions.assertThat(o.status()).isEqualTo(OrderStatus.DRAFT),
                (o) -> Assertions.assertThat(o.readyAt()).isNull()
        );
    }

    @Test
    void givenPlacedOrder_whenMarkAsReady_shouldThrowExceptionAndNotChangeState() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();

        Assertions.assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(order::markAsReady);

        Assertions.assertWith(order,
                (o) -> Assertions.assertThat(o.status()).isEqualTo(OrderStatus.PLACED),
                (o) -> Assertions.assertThat(o.readyAt()).isNull()
        );
    }

    @Test
    void givenReadyOrder_whenMarkAsReady_shouldThrowExceptionAndNotChangeState() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.READY).build();

        Assertions.assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(order::markAsReady);

        Assertions.assertWith(order,
                (o) -> Assertions.assertThat(o.status()).isEqualTo(OrderStatus.READY),
                (o) -> Assertions.assertThat(o.readyAt()).isNotNull()
        );
    }
}