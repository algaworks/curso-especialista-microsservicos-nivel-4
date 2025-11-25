package com.algaworks.algashop.ordering.core.ports.in.shoppingcart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartItemOutput {
	private UUID id;
	private UUID productId;
	private String name;
	private BigDecimal price;
	private Integer quantity;
	private BigDecimal totalAmount;
	private Boolean available;
}
