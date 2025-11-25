package com.algaworks.algashop.billing.application.invoice.management;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressData {
	@NotBlank
	private String street;
	@NotBlank
	private String number;
	private String complement;
	@NotBlank
	private String neighborhood;
	@NotBlank
	private String city;
	@NotBlank
	private String state;
	@NotBlank
	private String zipCode;
}
