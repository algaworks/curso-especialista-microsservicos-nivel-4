package com.algaworks.algashop.ordering.core.application.checkout;

import com.algaworks.algashop.ordering.core.application.AbstractApplicationIT;
import com.algaworks.algashop.ordering.core.domain.model.commons.Money;
import com.algaworks.algashop.ordering.core.domain.model.commons.Quantity;
import com.algaworks.algashop.ordering.core.domain.model.customer.CustomerTestDataBuilder;
import com.algaworks.algashop.ordering.core.domain.model.customer.Customers;
import com.algaworks.algashop.ordering.core.domain.model.order.*;
import com.algaworks.algashop.ordering.core.domain.model.order.shipping.OriginAddressService;
import com.algaworks.algashop.ordering.core.domain.model.order.shipping.ShippingCostService;
import com.algaworks.algashop.ordering.core.domain.model.product.Product;
import com.algaworks.algashop.ordering.core.domain.model.product.ProductTestDataBuilder;
import com.algaworks.algashop.ordering.core.domain.model.shoppingcart.*;
import com.algaworks.algashop.ordering.core.ports.in.checkout.CheckoutInput;
import com.algaworks.algashop.ordering.infrastructure.adapters.in.listener.order.OrderEventListener;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

class CheckoutApplicationServiceIT
        extends AbstractApplicationIT {

    @Autowired
    private CheckoutApplicationService service;

    @Autowired
    private Orders orders;

    @Autowired
    private ShoppingCarts shoppingCarts;

    @Autowired
    private Customers customers;

    @Autowired
    private CheckoutService checkoutService;

    @Autowired
    private OriginAddressService originAddressService;

    @MockitoBean
    private ShippingCostService shippingCostService;

    @MockitoSpyBean
    private OrderEventListener orderEventListener;

    @BeforeEach
    public void setup() {
        Mockito.when(shippingCostService.calculate(Mockito.any(ShippingCostService.CalculationRequest.class)))
                .thenReturn(new ShippingCostService.CalculationResult(
                        new Money("10.00"),
                        LocalDate.now().plusDays(3)
                ));

        if (!customers.exists(CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID)) {
            customers.add(CustomerTestDataBuilder.existingCustomer().build());
        }
    }

    @Test
    void shouldCheckout() {
        Product product = ProductTestDataBuilder.aProduct().inStock(true).build();

        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.aShoppingCart().withItems(false).build();
        shoppingCart.addItem(product, new Quantity(1));
        shoppingCarts.add(shoppingCart);

        CheckoutInput input = CheckoutInputTestDataBuilder.aCheckoutInput()
                .shoppingCartId(shoppingCart.id().value())
                .build();


        String orderId = service.checkout(input);

        Assertions.assertThat(orderId).isNotBlank();
        Assertions.assertThat(orders.exists(new OrderId(orderId))).isTrue();

        Optional<Order> createdOrder = orders.ofId(new OrderId(orderId));
        Assertions.assertThat(createdOrder).isPresent();
        Assertions.assertThat(createdOrder.get().status()).isEqualTo(OrderStatus.PLACED);
        Assertions.assertThat(createdOrder.get().totalAmount().value()).isGreaterThan(BigDecimal.ZERO);

        Optional<ShoppingCart> updatedCart = shoppingCarts.ofId(shoppingCart.id());
        Assertions.assertThat(updatedCart).isPresent();
        Assertions.assertThat(updatedCart.get().isEmpty()).isTrue();

        Mockito.verify(orderEventListener).listen(Mockito.any(OrderPlacedEvent.class));
    }

    @Test
    void shouldThrowShoppingCartNotFoundExceptionWhenCheckoutWithNonExistingShoppingCart() {
        CheckoutInput input = CheckoutInputTestDataBuilder.aCheckoutInput()
                .shoppingCartId(UUID.randomUUID())
                .build();

        Assertions.assertThatExceptionOfType(ShoppingCartNotFoundException.class)
                .isThrownBy(() -> service.checkout(input));
    }

    @Test
    void shouldThrowShoppingCartCantProceedToCheckoutExceptionWhenCartIsEmpty() {
        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.aShoppingCart().withItems(false).build();
        shoppingCarts.add(shoppingCart);

        CheckoutInput input = CheckoutInputTestDataBuilder.aCheckoutInput()
                .shoppingCartId(shoppingCart.id().value())
                .build();

        Assertions.assertThatExceptionOfType(ShoppingCartCantProceedToCheckoutException.class)
                .isThrownBy(() -> service.checkout(input));
    }

    @Test
    void shouldThrowShoppingCartCantProceedToCheckoutExceptionWhenCartContainsUnavailableItems() {
        Product product = ProductTestDataBuilder.aProduct().inStock(true).build();
        Product unavailableProduct = ProductTestDataBuilder.aProduct().id(product.id()).inStock(false).build();

        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.aShoppingCart().withItems(false).build();
        shoppingCart.addItem(product, new Quantity(1));
        shoppingCart.refreshItem(unavailableProduct);
        shoppingCarts.add(shoppingCart);

        CheckoutInput input = CheckoutInputTestDataBuilder.aCheckoutInput()
                .shoppingCartId(shoppingCart.id().value())
                .build();

        Assertions.assertThatExceptionOfType(ShoppingCartCantProceedToCheckoutException.class)
                .isThrownBy(() -> service.checkout(input));
    }
}
