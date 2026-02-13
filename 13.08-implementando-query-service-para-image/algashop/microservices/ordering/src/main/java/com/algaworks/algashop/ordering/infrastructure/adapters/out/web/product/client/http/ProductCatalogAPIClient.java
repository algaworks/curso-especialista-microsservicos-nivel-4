package com.algaworks.algashop.ordering.infrastructure.adapters.out.web.product.client.http;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

import java.util.UUID;

public interface ProductCatalogAPIClient {
    @GetExchange(value = "/api/v1/products/{productId}", accept = "application/json")
    ProductResponse getById(@PathVariable UUID productId);
}
