package com.algaworks.algashop.ordering.core.domain.model.commons;

import java.util.Objects;

public record FullName(String firstName, String lastName) {
    public FullName(String firstName, String lastName) {
        Objects.requireNonNull(firstName);
        Objects.requireNonNull(lastName);

        if (firstName.isBlank()) {
            throw new IllegalArgumentException();
        }

        if (lastName.isBlank()) {
            throw new IllegalArgumentException();
        }

        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
