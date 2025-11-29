package com.algaworks.algashop.product.catalog.domain.model.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.UUID;

public interface ProductRepository extends MongoRepository<Product, UUID> {

    @Query(value = "{'enabled': ?0}", fields = "{'name': 1}")
    Page<ProductNameProjection> findAllByEnabled(Boolean enabled, Pageable pageable);
}
