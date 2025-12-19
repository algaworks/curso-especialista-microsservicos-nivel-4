package com.algaworks.algashop.product.catalog.domain.model.product;

import com.algaworks.algashop.product.catalog.infrastructure.persistence.MongoConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.mongodb.test.autoconfigure.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@Import(MongoConfig.class)
@Slf4j
class ProductRepositoryIT {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void shouldFilter() {
        Page<ProductNameProjection> products = productRepository
                .findAllByEnabled(true, PageRequest.of(0, 3));
        products.forEach(p -> log.info("Product - Id: {} Name: {}", p.id(), p.name()));
    }

}