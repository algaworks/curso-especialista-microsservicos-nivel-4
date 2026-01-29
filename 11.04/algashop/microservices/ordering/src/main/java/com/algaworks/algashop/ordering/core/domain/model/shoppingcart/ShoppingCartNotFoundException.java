package com.algaworks.algashop.ordering.core.domain.model.shoppingcart;

import com.algaworks.algashop.ordering.core.domain.model.DomainEntityNotFoundException;

import java.util.UUID;

public class ShoppingCartNotFoundException extends DomainEntityNotFoundException {

    public ShoppingCartNotFoundException(UUID shoppingCartId) {
        this("Shopping cart with ID " + shoppingCartId + " not found.");
    }

    public ShoppingCartNotFoundException(String message) {
        super(message);
    }

    public static ShoppingCartNotFoundException ofCustomer(UUID customerId) {
        return new ShoppingCartNotFoundException("Shopping cart for customer ID " + customerId + " not found.");
    }
}
