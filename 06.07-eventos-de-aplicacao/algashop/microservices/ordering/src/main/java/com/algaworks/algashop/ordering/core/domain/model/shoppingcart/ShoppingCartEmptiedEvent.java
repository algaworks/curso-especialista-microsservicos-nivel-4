package com.algaworks.algashop.ordering.core.domain.model.shoppingcart;

import com.algaworks.algashop.ordering.core.domain.model.customer.CustomerId;

import java.time.OffsetDateTime;

public record ShoppingCartEmptiedEvent(
    ShoppingCartId shoppingCartId,
    CustomerId customerId,
    OffsetDateTime emptiedAt
) {}
