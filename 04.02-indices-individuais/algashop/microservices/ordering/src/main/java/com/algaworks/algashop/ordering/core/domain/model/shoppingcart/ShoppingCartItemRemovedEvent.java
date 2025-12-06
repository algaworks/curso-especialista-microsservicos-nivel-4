package com.algaworks.algashop.ordering.core.domain.model.shoppingcart;

import com.algaworks.algashop.ordering.core.domain.model.customer.CustomerId;
import com.algaworks.algashop.ordering.core.domain.model.product.ProductId;

import java.time.OffsetDateTime;

public record ShoppingCartItemRemovedEvent(
    ShoppingCartId shoppingCartId,
    CustomerId customerId,
    ProductId productId,
    OffsetDateTime removedAt
) {}
