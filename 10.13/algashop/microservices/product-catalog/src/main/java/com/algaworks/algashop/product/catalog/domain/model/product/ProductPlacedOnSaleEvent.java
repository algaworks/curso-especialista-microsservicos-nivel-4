package com.algaworks.algashop.product.catalog.domain.model.product;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@ToString
@Builder
public class ProductPlacedOnSaleEvent {
    private UUID productId;
    private BigDecimal regularPrice;
    private BigDecimal salePrice;
    @Builder.Default
    private OffsetDateTime placedOnSaleAt = OffsetDateTime.now();
}
