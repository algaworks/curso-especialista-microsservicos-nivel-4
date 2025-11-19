package com.algaworks.algashop.product.catalog.domain.model.category;

import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<Category, MongoId> {
}
