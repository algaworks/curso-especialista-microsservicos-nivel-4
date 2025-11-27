package com.algaworks.algashop.ordering.core.domain.model.order;

import com.algaworks.algashop.ordering.core.domain.model.commons.Address;
import com.algaworks.algashop.ordering.core.domain.model.commons.Money;
import lombok.Builder;

import java.time.LocalDate;
import java.util.Objects;

@Builder(toBuilder = true)
public record Shipping(Money cost, LocalDate expectedDate, Recipient recipient, Address address) {
	public Shipping {
		Objects.requireNonNull(address);
		Objects.requireNonNull(recipient);
		Objects.requireNonNull(cost);
		Objects.requireNonNull(expectedDate);
	}
}