package com.algaworks.algashop.product.catalog.application.product.management;

import com.algaworks.algashop.product.catalog.application.ResourceNotFoundException;
import com.algaworks.algashop.product.catalog.domain.model.category.Category;
import com.algaworks.algashop.product.catalog.domain.model.category.CategoryNotFoundException;
import com.algaworks.algashop.product.catalog.domain.model.category.CategoryRepository;
import com.algaworks.algashop.product.catalog.domain.model.product.Product;
import com.algaworks.algashop.product.catalog.domain.model.product.ProductNotFoundException;
import com.algaworks.algashop.product.catalog.domain.model.product.ProductRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductManagementApplicationService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public UUID create(ProductInput input) {
        Product product = mapToProduct(input);
        productRepository.save(product);
        return product.getId();
    }

    private Product mapToProduct(ProductInput input) {
        Category category = findCategory(input.getCategoryId());
        return Product.builder()
                .name(input.getName())
                .brand(input.getBrand())
                .description(input.getDescription())
                .regularPrice(input.getRegularPrice())
                .salePrice(input.getSalePrice())
                .enabled(input.getEnabled())
                .category(category)
                .build();
    }

    public void update(UUID productId, ProductInput input) {
        Product product = findProduct(productId);
        Category category = findCategory(input.getCategoryId());

        updateProduct(product, input);
        product.setCategory(category);

        productRepository.save(product);
    }

    public void disable(UUID productId) {
        Product product = findProduct(productId);
        product.disable();
        productRepository.save(product);
    }

    public void enable(UUID productId) {
        Product product = findProduct(productId);
        product.enable();
        productRepository.save(product);
    }

    private void updateProduct(Product product, ProductInput input) {
        product.setName(input.getName());
        product.setBrand(input.getBrand());
        product.setDescription(input.getDescription());
        product.setRegularPrice(input.getRegularPrice());
        product.setSalePrice(input.getSalePrice());
        product.setEnabled(input.getEnabled());
    }

    private Product findProduct(UUID productId) {
        return productRepository.findById(productId).orElseThrow(()-> new ProductNotFoundException(productId));
    }

    private Category findCategory(@NotNull UUID categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(()-> new CategoryNotFoundException(categoryId));
    }
}
