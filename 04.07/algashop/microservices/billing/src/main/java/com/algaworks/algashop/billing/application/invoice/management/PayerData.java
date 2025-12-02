package com.algaworks.algashop.billing.application.invoice.management;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayerData {
	@NotBlank
	private String fullName;
	@NotBlank
	private String document;
	@NotBlank
	private String email;
	@NotBlank
	private String phone;
	@NotNull
	@Valid
	private AddressData address;
}