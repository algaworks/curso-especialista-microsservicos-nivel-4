package com.algaworks.algashop.product.catalog.domain.model.category;

import com.algaworks.algashop.product.catalog.domain.model.DomainEntityNotFoundException;

import java.util.UUID;

public class CategoryNotFoundException extends DomainEntityNotFoundException {
    public CategoryNotFoundException(UUID categoryId) {
        super(String.format("Category with id %s was not found", categoryId));
    }
}
