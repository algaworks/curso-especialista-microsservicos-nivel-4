package com.algaworks.algashop.product.catalog.domain.model.product;

import com.algaworks.algashop.product.catalog.domain.model.category.Category;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductCategory {
    private UUID id;
    private String name;
    private Boolean enabled;

    public static ProductCategory of(Category category) {
        return new ProductCategory(category.getId(), category.getName(), category.getEnabled());
    }
}
