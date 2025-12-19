package com.algaworks.algashop.product.catalog.domain.model.product;

import java.util.UUID;

public interface QuantityInStockAdjustment {
    Result increase(UUID productId, int quantity);
    Result decrease(UUID productId, int quantity);

    record Result(
            UUID productId,
            int previousQuantity,
            int newQuantity
    ) {}
}
