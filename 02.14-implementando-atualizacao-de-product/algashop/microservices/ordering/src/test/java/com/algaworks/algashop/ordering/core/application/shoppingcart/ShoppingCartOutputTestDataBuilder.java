package com.algaworks.algashop.ordering.core.application.shoppingcart;

import com.algaworks.algashop.ordering.core.ports.in.shoppingcart.ShoppingCartItemOutput;
import com.algaworks.algashop.ordering.core.ports.in.shoppingcart.ShoppingCartOutput;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class ShoppingCartOutputTestDataBuilder {
    public static ShoppingCartOutput.ShoppingCartOutputBuilder aShoppingCart() {
        return ShoppingCartOutput.builder()
                .id(UUID.randomUUID())
                .customerId(UUID.randomUUID())
                .totalItems(3)
                .totalAmount(new BigDecimal(1250))
                .items(List.of(
                        existingItem().build(),
                        existingItemAlt().build()
                ));
    }

    public static ShoppingCartItemOutput.ShoppingCartItemOutputBuilder existingItem() {
        return ShoppingCartItemOutput.builder()
                .id(UUID.randomUUID())
                .productId(UUID.randomUUID())
                .price(new BigDecimal(500))
                .quantity(2)
                .totalAmount(new BigDecimal(1000))
                .available(true)
                .name("Notebook");
    }

    public static ShoppingCartItemOutput.ShoppingCartItemOutputBuilder existingItemAlt() {
        return ShoppingCartItemOutput.builder()
                .id(UUID.randomUUID())
                .productId(UUID.randomUUID())
                .price(new BigDecimal(250))
                .quantity(1)
                .totalAmount(new BigDecimal(250))
                .available(true)
                .name("Mouse pad");
    }
}
