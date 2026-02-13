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
public class ProductPriceChangedEvent {
    private UUID productId;
    private BigDecimal oldRegularPrice;
    private BigDecimal oldSalePrice;
    private BigDecimal newRegularPrice;
    private BigDecimal newSalePrice;

    @Builder.Default
    private OffsetDateTime changedAt = OffsetDateTime.now();
}
