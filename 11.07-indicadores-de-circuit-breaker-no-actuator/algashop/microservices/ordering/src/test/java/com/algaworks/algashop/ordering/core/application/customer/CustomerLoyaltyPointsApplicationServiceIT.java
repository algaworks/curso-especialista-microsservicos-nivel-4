package com.algaworks.algashop.ordering.core.application.customer;

import com.algaworks.algashop.ordering.core.application.AbstractApplicationIT;
import com.algaworks.algashop.ordering.core.domain.model.commons.Email;
import com.algaworks.algashop.ordering.core.domain.model.commons.Money;
import com.algaworks.algashop.ordering.core.domain.model.commons.Quantity;
import com.algaworks.algashop.ordering.core.domain.model.customer.*;
import com.algaworks.algashop.ordering.core.domain.model.order.*;
import com.algaworks.algashop.ordering.core.domain.model.product.Product;
import com.algaworks.algashop.ordering.core.domain.model.product.ProductTestDataBuilder;
import com.algaworks.algashop.ordering.infrastructure.adapters.in.listener.customer.CustomerEventListener;
import io.hypersistence.tsid.TSID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.UUID;

class CustomerLoyaltyPointsApplicationServiceIT
        extends AbstractApplicationIT {

    @Autowired
    private CustomerLoyaltyPointsApplicationService loyaltyPointsService;

    @Autowired
    private Customers customers;

    @Autowired
    private Orders orders;

    @MockitoBean
    private CustomerEventListener customerEventListener;
    
    @Test
    void shouldAddLoyaltyPointsToCustomerWhenOrderIsValidAndReady() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);

        Order order = OrderTestDataBuilder.anOrder()
                .customerId(customer.id())
                .status(OrderStatus.DRAFT)
                .withItems(false)
                .build();
        Product product = ProductTestDataBuilder.aProduct().price(new Money("2500")).build();

        order.addItem(product, new Quantity(1));
        order.place();
        order.markAsPaid();
        order.markAsReady();

        orders.add(order);

        loyaltyPointsService.addLoyaltyPoints(customer.id().value(), order.id().toString());

        Customer updatedCustomer = customers.ofId(customer.id()).orElseThrow();
        Assertions.assertThat(updatedCustomer).isNotNull();
        Assertions.assertThat(updatedCustomer.loyaltyPoints()).isEqualTo(new LoyaltyPoints(10));
    }
    
    @Test
    void shouldThrowCustomerNotFoundExceptionWhenCustomerIdDoesNotExist() {
        UUID nonExistingCustomerId = UUID.randomUUID();

        Customer dummyCustomer = CustomerTestDataBuilder.brandNewCustomer()
                .email(new Email("dummy@example.com")).build();
        customers.add(dummyCustomer);

        Order order = OrderTestDataBuilder.anOrder()
                .customerId(dummyCustomer.id())
                .status(OrderStatus.READY)
                .build();
        orders.add(order);

        Assertions.assertThatExceptionOfType(CustomerNotFoundException.class)
            .isThrownBy(() -> loyaltyPointsService.addLoyaltyPoints(nonExistingCustomerId, order.id().toString()));
    }
    
    @Test
    void shouldThrowOrderNotFoundExceptionWhenOrderIdDoesNotExist() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);
        String nonExistingOrderId = TSID.fast().toString();
        Assertions.assertThatExceptionOfType(OrderNotFoundException.class)
            .isThrownBy(() -> loyaltyPointsService.addLoyaltyPoints(customer.id().value(), nonExistingOrderId));
    }

    @Test
    void shouldThrowCustomerArchivedExceptionWhenCustomerIsArchived() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        customers.add(customer);
        customer.archive();
        customers.add(customer);

        Order order = OrderTestDataBuilder.anOrder()
                .customerId(customer.id())
                .status(OrderStatus.READY)
                .build();
        orders.add(order);
        
        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
            .isThrownBy(() -> loyaltyPointsService.addLoyaltyPoints(customer.id().value(), order.id().toString()));
    }

    @Test
    void shouldThrowOrderNotBelongsToCustomerExceptionWhenOrderCustomerIdDoesNotMatch() {
        Customer customerA = CustomerTestDataBuilder.existingCustomer()
                .id(new CustomerId())
                .email(new Email("customerA@example.com")).build();
        Customer customerB = CustomerTestDataBuilder.existingCustomer()
                .id(new CustomerId())
                .email(new Email("customerB@example.com")).build();
        customers.add(customerA);
        customers.add(customerB);
        
        Order orderForCustomerB = OrderTestDataBuilder.anOrder()
                .customerId(customerB.id())
                .status(OrderStatus.READY)
                .build();

        orders.add(orderForCustomerB);

        Assertions.assertThatExceptionOfType(OrderNotBelongsToCustomerException.class)
            .isThrownBy(() -> loyaltyPointsService.addLoyaltyPoints(customerA.id().value(),
                    orderForCustomerB.id().toString()));
    }

    @Test
    void shouldThrowCantAddLoyaltyPointsOrderIsNotReadyWhenOrderIsNotReady() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);

        Order order = OrderTestDataBuilder.anOrder()
                .customerId(customer.id())
                .status(OrderStatus.PLACED)
                .build();
        orders.add(order);

        Assertions.assertThatExceptionOfType(CantAddLoyaltyPointsOrderIsNotReady.class)
            .isThrownBy(() -> loyaltyPointsService.addLoyaltyPoints(customer.id().value(), order.id().toString()));
    }

    @Test
    void shouldNotAddLoyaltyPointsWhenOrderAmountIsLessThanThreshold() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);

        Order order = OrderTestDataBuilder.anOrder()
                .customerId(customer.id())
                .status(OrderStatus.DRAFT)
                .withItems(false)
                .build();
        Product product = ProductTestDataBuilder.aProduct().price(new Money("500")).build();

        order.addItem(product, new Quantity(1));
        order.place();
        order.markAsPaid();
        order.markAsReady();

        orders.add(order);

        loyaltyPointsService.addLoyaltyPoints(customer.id().value(), order.id().toString());

        Customer updatedCustomer = customers.ofId(customer.id()).orElseThrow();
        Assertions.assertThat(updatedCustomer).isNotNull();
        Assertions.assertThat(updatedCustomer.loyaltyPoints()).isEqualTo(LoyaltyPoints.ZERO);
    }
}
