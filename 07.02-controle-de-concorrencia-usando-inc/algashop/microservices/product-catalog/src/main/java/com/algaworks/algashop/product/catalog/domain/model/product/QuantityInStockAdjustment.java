package com.algaworks.algashop.product.catalog.domain.model.product;

import java.util.UUID;

public interface QuantityInStockAdjustment {
    void increase(UUID productId, int quantity);
    void decrease(UUID productId, int quantity);
}
