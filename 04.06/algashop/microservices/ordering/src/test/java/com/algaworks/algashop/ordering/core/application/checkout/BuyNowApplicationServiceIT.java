package com.algaworks.algashop.ordering.core.application.checkout;

import com.algaworks.algashop.ordering.core.application.AbstractApplicationIT;
import com.algaworks.algashop.ordering.core.domain.model.commons.Money;
import com.algaworks.algashop.ordering.core.domain.model.customer.CustomerTestDataBuilder;
import com.algaworks.algashop.ordering.core.domain.model.customer.Customers;
import com.algaworks.algashop.ordering.core.domain.model.order.OrderId;
import com.algaworks.algashop.ordering.core.domain.model.order.Orders;
import com.algaworks.algashop.ordering.core.domain.model.order.shipping.ShippingCostService;
import com.algaworks.algashop.ordering.core.domain.model.product.Product;
import com.algaworks.algashop.ordering.core.domain.model.product.ProductCatalogService;
import com.algaworks.algashop.ordering.core.domain.model.product.ProductTestDataBuilder;
import com.algaworks.algashop.ordering.core.ports.in.checkout.BuyNowInput;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.Optional;

class BuyNowApplicationServiceIT
        extends AbstractApplicationIT {

    @Autowired
    private BuyNowApplicationService buyNowApplicationService;

    @Autowired
    private Orders orders;

    @Autowired
    private Customers customers;

    @MockitoBean
    private ProductCatalogService productCatalogService;

    @MockitoBean
    private ShippingCostService shippingCostService;

    @BeforeEach
    public void setup() {
        if (!customers.exists(CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID)) {
            customers.add(CustomerTestDataBuilder.existingCustomer().build());
        }
    }

    @Test
    public void shouldBuyNow() {
        Product product = ProductTestDataBuilder.aProduct().build();
        Mockito.when(productCatalogService.ofId(product.id())).thenReturn(Optional.of(product));

        Mockito.when(shippingCostService.calculate(Mockito.any(ShippingCostService.CalculationRequest.class)))
                .thenReturn(new ShippingCostService.CalculationResult(
                        new Money("10.00"),
                        LocalDate.now().plusDays(3)
                ));

        BuyNowInput input = BuyNowInputTestDataBuilder.aBuyNowInput().build();

        String orderId = buyNowApplicationService.buyNow(input);

        Assertions.assertThat(orderId).isNotBlank();
        Assertions.assertThat(orders.exists(new OrderId(orderId))).isTrue();
    }

}