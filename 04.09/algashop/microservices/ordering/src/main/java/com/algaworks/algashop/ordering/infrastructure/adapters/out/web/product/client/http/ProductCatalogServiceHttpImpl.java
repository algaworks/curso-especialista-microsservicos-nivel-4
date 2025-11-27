package com.algaworks.algashop.ordering.infrastructure.adapters.out.web.product.client.http;

import com.algaworks.algashop.ordering.core.domain.model.commons.Money;
import com.algaworks.algashop.ordering.core.domain.model.product.Product;
import com.algaworks.algashop.ordering.core.domain.model.product.ProductCatalogService;
import com.algaworks.algashop.ordering.core.domain.model.product.ProductId;
import com.algaworks.algashop.ordering.core.domain.model.product.ProductName;
import com.algaworks.algashop.ordering.infrastructure.adapters.in.web.exceptionhandler.BadGatewayException;
import com.algaworks.algashop.ordering.infrastructure.adapters.in.web.exceptionhandler.GatewayTimeoutException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductCatalogServiceHttpImpl implements ProductCatalogService {

    private final ProductCatalogAPIClient productCatalogAPIClient;

    @Override
    public Optional<Product> ofId(ProductId productId) {
        ProductResponse productResponse;
        try {
            productResponse = productCatalogAPIClient.getById(productId.value());
        } catch (ResourceAccessException e) {
            throw new GatewayTimeoutException("Product Catalog API Timeout", e);
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        } catch (HttpClientErrorException e) {
            throw new BadGatewayException("Product Catalog API Bad Gateway", e);
        }

        return Optional.of(
                Product.builder()
                        .id(new ProductId(productResponse.getId()))
                        .name(new ProductName(productResponse.getName()))
                        .inStock(productResponse.getInStock())
                        .price(new Money(productResponse.getSalePrice()))
                        .build()
        );
    }
}
