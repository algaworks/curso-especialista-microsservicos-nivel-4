package com.algaworks.algashop.product.catalog.application.product.query;

import com.algaworks.algashop.product.catalog.application.PageModel;
import org.springframework.cache.annotation.Cacheable;

import java.util.UUID;

public interface ProductQueryService {
    @Cacheable(cacheNames = "algashop:products:v1", key = "#productId")
    ProductDetailOutput findById(UUID productId);
    PageModel<ProductSummaryOutput> filter(ProductFilter filter);
}
