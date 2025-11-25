package com.algaworks.algashop.ordering.core.ports.in.shoppingcart;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartItemInput {

	@NotNull
	@Positive
	private Integer quantity;

	@NotNull
	private UUID productId;
	//Alimentado via PathVariable
	private UUID shoppingCartId;
}