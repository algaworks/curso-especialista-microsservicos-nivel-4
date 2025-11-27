package com.algaworks.algashop.product.catalog.infrastructure.persistence.product;

import com.algaworks.algashop.product.catalog.application.PageModel;
import com.algaworks.algashop.product.catalog.application.ResourceNotFoundException;
import com.algaworks.algashop.product.catalog.application.product.query.ProductDetailOutput;
import com.algaworks.algashop.product.catalog.application.product.query.ProductQueryService;
import com.algaworks.algashop.product.catalog.application.product.query.ProductSummaryOutput;
import com.algaworks.algashop.product.catalog.application.utility.Mapper;
import com.algaworks.algashop.product.catalog.domain.model.product.Product;
import com.algaworks.algashop.product.catalog.domain.model.product.ProductNotFoundException;
import com.algaworks.algashop.product.catalog.domain.model.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductQueryServiceImpl implements ProductQueryService {

    private final ProductRepository productRepository;
    private final Mapper mapper;

    @Override
    public ProductDetailOutput findById(UUID productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
        return mapper.convert(product, ProductDetailOutput.class);
    }

    @Override
    public PageModel<ProductSummaryOutput> filter(Integer size, Integer number) {
        Page<Product> products = productRepository.findAll(PageRequest.of(number, size));
        Page<ProductSummaryOutput> productOutputs = products.map(p -> mapper.convert(p, ProductSummaryOutput.class));
        return PageModel.of(productOutputs);
    }
}
