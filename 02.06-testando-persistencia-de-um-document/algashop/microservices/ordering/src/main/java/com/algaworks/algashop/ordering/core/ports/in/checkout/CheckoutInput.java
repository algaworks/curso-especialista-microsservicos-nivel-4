package com.algaworks.algashop.ordering.core.ports.in.checkout;

import com.algaworks.algashop.ordering.core.ports.in.order.BillingData;
import com.algaworks.algashop.ordering.core.ports.in.order.ShippingInput;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckoutInput {
	@NotNull
	private UUID shoppingCartId;

	@NotBlank
	private String paymentMethod;

	@Valid
	@NotNull
	private ShippingInput shipping;

	@Valid
	@NotNull
	private BillingData billing;

	private UUID creditCardId;
}