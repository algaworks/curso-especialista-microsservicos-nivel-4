package com.algaworks.algashop.ordering.core.domain.model.shoppingcart;

import com.algaworks.algashop.ordering.core.domain.model.IdGenerator;

import java.util.Objects;
import java.util.UUID;

public record ShoppingCartId(UUID value) {

    public ShoppingCartId {
        Objects.requireNonNull(value);
    }

    public ShoppingCartId() {
        this(IdGenerator.generateTimeBasedUUID());
    }

    public ShoppingCartId(String value) {
        this(UUID.fromString(value));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
