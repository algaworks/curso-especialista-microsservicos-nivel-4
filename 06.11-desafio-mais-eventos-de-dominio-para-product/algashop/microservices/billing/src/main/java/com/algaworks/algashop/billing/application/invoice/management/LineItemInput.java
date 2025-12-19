package com.algaworks.algashop.billing.application.invoice.management;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LineItemInput {
	@NotBlank
	private String name;
	@NotNull
	private BigDecimal amount;
	@NotNull
	@Positive
	private Integer quantity;
}
