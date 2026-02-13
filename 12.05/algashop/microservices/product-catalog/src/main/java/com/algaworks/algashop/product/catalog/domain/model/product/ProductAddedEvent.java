package com.algaworks.algashop.product.catalog.domain.model.product;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@ToString
@Builder
public class ProductAddedEvent {
	private UUID productId;
	@Builder.Default
	private OffsetDateTime addedAt = OffsetDateTime.now();
}