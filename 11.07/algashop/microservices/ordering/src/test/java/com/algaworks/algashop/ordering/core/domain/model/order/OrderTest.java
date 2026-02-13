package com.algaworks.algashop.ordering.core.domain.model.order;

import com.algaworks.algashop.ordering.core.domain.model.commons.Money;
import com.algaworks.algashop.ordering.core.domain.model.commons.Quantity;
import com.algaworks.algashop.ordering.core.domain.model.customer.CustomerId;
import com.algaworks.algashop.ordering.core.domain.model.product.*;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

class OrderTest {

    @Test
    public void shouldGenerateDraftOrder() {
        CustomerId customerId = new CustomerId();
        Order order = Order.draft(customerId);

        Assertions.assertWith(order,
                o -> Assertions.assertThat(o.id()).isNotNull(),
                o -> Assertions.assertThat(o.customerId()).isEqualTo(customerId),
                o -> Assertions.assertThat(o.totalAmount()).isEqualTo(Money.ZERO),
                o -> Assertions.assertThat(o.totalItems()).isEqualTo(Quantity.ZERO),
                o -> Assertions.assertThat(o.isDraft()).isTrue(),
                o -> Assertions.assertThat(o.items()).isEmpty(),

                o -> Assertions.assertThat(o.placedAt()).isNull(),
                o -> Assertions.assertThat(o.paidAt()).isNull(),
                o -> Assertions.assertThat(o.canceledAt()).isNull(),
                o -> Assertions.assertThat(o.readyAt()).isNull(),
                o -> Assertions.assertThat(o.billing()).isNull(),
                o -> Assertions.assertThat(o.shipping()).isNull(),
                o -> Assertions.assertThat(o.paymentMethod()).isNull()

        );
    }

    @Test
    public void shouldAddItem() {
        Order order = Order.draft(new CustomerId());
        Product product = ProductTestDataBuilder.aProductAltMousePad().build();
        ProductId productId = product.id();

        order.addItem(product, new Quantity(1));

        Assertions.assertThat(order.items().size()).isEqualTo(1);

        OrderItem orderItem = order.items().iterator().next();

        Assertions.assertWith(orderItem,
                (i) -> Assertions.assertThat(i.id()).isNotNull(),
                (i) -> Assertions.assertThat(i.productName()).isEqualTo(new ProductName("Mouse Pad")),
                (i) -> Assertions.assertThat(i.productId()).isEqualTo(productId),
                (i) -> Assertions.assertThat(i.price()).isEqualTo(new Money("100")),
                (i) -> Assertions.assertThat(i.quantity()).isEqualTo(new Quantity(1))
        );
    }

    @Test
    public void shouldGenerateExceptionWhenTryToChangeItemSet() {
        Order order = Order.draft(new CustomerId());
        Product product = ProductTestDataBuilder.aProductAltMousePad().build();

        order.addItem(product, new Quantity(1));

        Set<OrderItem> items = order.items();

        Assertions.assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(items::clear);
    }

    @Test
    public void shouldCalculateTotals() {
        Order order = Order.draft(new CustomerId());

        order.addItem(
                ProductTestDataBuilder.aProductAltMousePad().build(),
                new Quantity(2)
        );

        order.addItem(
                ProductTestDataBuilder.aProductAltRamMemory().build(),
                new Quantity(1)
        );

        Assertions.assertThat(order.totalAmount()).isEqualTo(new Money("400"));
        Assertions.assertThat(order.totalItems()).isEqualTo(new Quantity(3));
    }

    @Test
    public void givenDraftOrder_whenPlace_shouldChangeToPlaced() {
        Order order = OrderTestDataBuilder.anOrder().build();
        order.place();
        Assertions.assertThat(order.isPlaced()).isTrue();
    }

    @Test
    public void givenPlacedOrder_whenMarkAsPaid_shouldChangeToPaid() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        order.markAsPaid();
        Assertions.assertThat(order.isPaid()).isTrue();
        Assertions.assertThat(order.paidAt()).isNotNull();
    }

    @Test
    public void givenPlacedOrder_whenTryToPlace_shouldGenerateException() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        Assertions.assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(order::place);
    }

    @Test
    public void givenDraftOrder_whenChangePaymentMethod_shouldAllowChange() {
        Order order = Order.draft(new CustomerId());
        order.changePaymentMethod(PaymentMethod.CREDIT_CARD, new CreditCardId());
        Assertions.assertWith(order.paymentMethod()).isEqualTo(PaymentMethod.CREDIT_CARD);
    }

    @Test
    public void givenDraftOrder_whenChangeBilling_shouldAllowChange() {
        Billing billing = OrderTestDataBuilder.aBilling();
        Order order = Order.draft(new CustomerId());
        order.changeBilling(billing);

        Assertions.assertThat(order.billing()).isEqualTo(billing);
    }

    @Test
    public void givenDraftOrder_whenChangeShipping_shouldAllowChange() {
        Shipping shipping = OrderTestDataBuilder.aShipping();
        Order order = Order.draft(new CustomerId());
        Money expectedTotalAmount = order.totalAmount().add(shipping.cost());

        order.changeShipping(shipping);

        Assertions.assertWith(order,
                o -> Assertions.assertThat(o.shipping()).isEqualTo(shipping),
                o -> Assertions.assertThat(o.totalAmount()).isEqualTo(expectedTotalAmount)
        );
    }

    @Test
    public void givenDraftOrderAndDeliveryDateInThePast_whenChangeShipping_shouldNotAllowChange() {
        LocalDate expectedDeliveryDate = LocalDate.now().minusDays(2);

        Shipping shipping = OrderTestDataBuilder.aShipping().toBuilder()
                .expectedDate(expectedDeliveryDate)
                .build();

        Order order = Order.draft(new CustomerId());

        Assertions.assertThatExceptionOfType(OrderInvalidShippingDeliveryDateException.class)
                .isThrownBy(()-> order.changeShipping(shipping));
    }

    @Test
    public void givenDraftOrder_whenChangeItem_shouldRecalculate() {
        Order order = Order.draft(new CustomerId());

        order.addItem(
                ProductTestDataBuilder.aProductAltMousePad().build(),
                new Quantity(3)
        );

        OrderItem orderItem = order.items().iterator().next();

        order.changeItemQuantity(orderItem.id(), new Quantity(5));

        Assertions.assertWith(order,
                (o) -> Assertions.assertThat(o.totalAmount()).isEqualTo(new Money("500")),
                (o) -> Assertions.assertThat(o.totalItems()).isEqualTo(new Quantity(5))
        );
    }

    @Test
    public void givenOutOfStockProduct_whenTryToAddToAnOrder_shouldNotAllow() {
        Order order = Order.draft(new CustomerId());

        ThrowableAssert.ThrowingCallable addItemTask = () -> order.addItem(
                ProductTestDataBuilder.aProductUnavailable().build(),
                new Quantity(1)
        );

        Assertions.assertThatExceptionOfType(ProductOutOfStockException.class).isThrownBy(addItemTask);
    }
  
}