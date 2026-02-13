package com.algaworks.algashop.product.catalog.domain.model.category;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface CategoryRepository extends MongoRepository<Category, UUID> {
}
