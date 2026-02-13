package com.algaworks.algashop.product.catalog.application.category.query;

import com.algaworks.algashop.product.catalog.application.PageModel;
import org.springframework.cache.annotation.Cacheable;

import java.util.UUID;

public interface CategoryQueryService {
    @Cacheable(cacheNames = "algashop:categories-filter:v1",
            key = "'default'",
            condition = "#filter.isCacheable()")
    PageModel<CategoryDetailOutput> filter(CategoryFilter filter);
    
    @Cacheable(cacheNames = "algashop:categories:v1", key = "#categoryId")
    CategoryDetailOutput findById(UUID categoryId);
}
