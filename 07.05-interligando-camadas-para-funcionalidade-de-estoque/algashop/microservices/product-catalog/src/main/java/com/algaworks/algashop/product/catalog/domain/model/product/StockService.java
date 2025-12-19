package com.algaworks.algashop.product.catalog.domain.model.product;

import com.algaworks.algashop.product.catalog.domain.model.DomainEventPublisher;
import com.algaworks.algashop.product.catalog.domain.model.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StockService {

    private final QuantityInStockAdjustment quantityInStockAdjustment;
    private final DomainEventPublisher domainEventPublisher;

    public void restock(Product product, int quantity) {
        Objects.requireNonNull(product);
        if (quantity < 1) {
            throw new IllegalArgumentException();
        }

        QuantityInStockAdjustment.Result result;
        try {
            result = quantityInStockAdjustment.increase(product.getId(), quantity);
        } catch (Exception e) {
            throw new DomainException(String.format("Failed to restock product %s", product.getId()));
        }

        if (result.inRestocked()) {
            domainEventPublisher.publish(
                    ProductRestockedEvent.builder().productId(product.getId()).build()
            );
        }
    }

    public void withdraw(Product product, int quantity) {
        Objects.requireNonNull(product);
        if (quantity <1) {
            throw new IllegalArgumentException();
        }

        QuantityInStockAdjustment.Result result;
        try {
            result = quantityInStockAdjustment.decrease(product.getId(), quantity);
        } catch (Exception e) {
            throw new DomainException(String.format("Failed to withdrawn product %s from stock", product.getId()));
        }

        if (result.isOutOfStock()) {
            domainEventPublisher.publish(
                    ProductSoldOutEvent.builder().productId(product.getId()).build()
            );
        }
    }

}
