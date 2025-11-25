package com.algaworks.algashop.ordering.core.domain.model.commons;

import java.io.Serializable;
import java.util.Objects;

public record Quantity(Integer value) implements Serializable, Comparable<Quantity> {
	public static final Quantity ZERO = new Quantity(0);

	public Quantity {
		Objects.requireNonNull(value);
		if (value < 0) {
			throw new IllegalArgumentException();
		}
	}

	public Quantity add(Quantity quantity) {
		Objects.requireNonNull(quantity);
		return new Quantity(this.value + quantity.value());
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

	@Override
	public int compareTo(Quantity o) {
		return this.value.compareTo(o.value);
	}
}