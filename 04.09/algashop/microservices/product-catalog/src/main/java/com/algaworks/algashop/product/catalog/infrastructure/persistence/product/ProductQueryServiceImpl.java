package com.algaworks.algashop.product.catalog.infrastructure.persistence.product;

import com.algaworks.algashop.product.catalog.application.PageModel;
import com.algaworks.algashop.product.catalog.application.ResourceNotFoundException;
import com.algaworks.algashop.product.catalog.application.product.query.ProductDetailOutput;
import com.algaworks.algashop.product.catalog.application.product.query.ProductFilter;
import com.algaworks.algashop.product.catalog.application.product.query.ProductQueryService;
import com.algaworks.algashop.product.catalog.application.product.query.ProductSummaryOutput;
import com.algaworks.algashop.product.catalog.application.utility.Mapper;
import com.algaworks.algashop.product.catalog.domain.model.product.Product;
import com.algaworks.algashop.product.catalog.domain.model.product.ProductNotFoundException;
import com.algaworks.algashop.product.catalog.domain.model.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductQueryServiceImpl implements ProductQueryService {

    private final ProductRepository productRepository;
    private final Mapper mapper;

    private final MongoOperations mongoOperations;

    @Override
    public ProductDetailOutput findById(UUID productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
        return mapper.convert(product, ProductDetailOutput.class);
    }

    @Override
    public PageModel<ProductSummaryOutput> filter(ProductFilter filter) {
        Query query = queryWith(filter);
        long totalItems = mongoOperations.count(query, Product.class);
        Sort sort = sortWith(filter);

        PageRequest pageRequest = PageRequest.of(filter.getPage(), filter.getSize(), sort);
        Query pagedQuery = query.with(pageRequest);

        List<Product> products;
        int totalPages = 0;

        if (totalItems > 0) {
            products = mongoOperations.find(pagedQuery, Product.class);
            totalPages = (int) Math.ceil((double) totalItems / pageRequest.getPageSize());
        } else {
            products = new ArrayList<>();
        }

        List<ProductSummaryOutput> productOutputs = products.stream()
                .map(p -> mapper.convert(p, ProductSummaryOutput.class))
                .collect(Collectors.toList());
        return PageModel.<ProductSummaryOutput>builder()
                .content(productOutputs)
                .number(pageRequest.getPageNumber())
                .size(pageRequest.getPageSize())
                .totalElements(totalItems)
                .totalPages(totalPages)
                .build();
    }

    private Sort sortWith(ProductFilter filter) {
        return Sort.by(filter.getSortDirectionOrDefault(),
                filter.getSortByPropertyOrDefault().getPropertyName());
    }

    private Query queryWith(ProductFilter filter) {
        Query query = new Query();

        if (filter.getEnabled() != null) {
            query.addCriteria(Criteria.where("enabled").is(filter.getEnabled()));
        }

        if (filter.getAddedAtFrom() != null && filter.getAddedAtTo() != null) {
            query.addCriteria(Criteria.where("addedAt")
                    .gte(filter.getAddedAtFrom())
                    .lte(filter.getAddedAtTo())
            );
        } else {
            if (filter.getAddedAtFrom() != null) {
                query.addCriteria(Criteria.where("addedAt").gte(filter.getAddedAtFrom()));
            } else if (filter.getAddedAtTo() != null) {
                query.addCriteria(Criteria.where("addedAt").lte(filter.getAddedAtTo()));
            }
        }

        if (filter.getPriceFrom() != null && filter.getPriceTo() != null ) {
            query.addCriteria(Criteria.where("salePrice")
                    .gte(filter.getPriceFrom())
                    .lte(filter.getPriceTo())
            );
        } else {
            if (filter.getPriceFrom() != null) {
                query.addCriteria(Criteria.where("salePrice").gte(filter.getPriceFrom()));
            } else if (filter.getPriceTo() != null) {
                query.addCriteria(Criteria.where("salePrice").lte(filter.getPriceTo()));
            }
        }

        return query;
    }
}
