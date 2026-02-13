package com.algaworks.algashop.product.catalog.domain.model.product;

import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Builder
public class ProductRestockedEvent {
    private UUID productId;

    @Builder.Default
    private OffsetDateTime restockedAt = OffsetDateTime.now();

}
