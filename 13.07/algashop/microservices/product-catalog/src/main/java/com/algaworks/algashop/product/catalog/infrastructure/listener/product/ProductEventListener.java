package com.algaworks.algashop.product.catalog.infrastructure.listener.product;

import com.algaworks.algashop.product.catalog.domain.model.product.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProductEventListener {

    @EventListener(ProductPriceChangedEvent.class)
    @Async
    public void handle(ProductPriceChangedEvent event) {
        log.info("ProductPriceChangedEvent " + event);
    }

    @EventListener(ProductPlacedOnSaleEvent.class)
    public void handle(ProductPlacedOnSaleEvent event) {
        log.info("ProductPlacedOnSaleEvent " + event);
    }

    @EventListener(ProductAddedEvent.class)
    public void handle(ProductAddedEvent event) {
        log.info("ProductAddedEvent " + event);
    }

    @EventListener(ProductDelistedEvent.class)
    public void handle(ProductDelistedEvent event) {
        log.info("ProductDelistedEvent  " + event);
    }

    @EventListener(ProductListedEvent.class)
    public void handle(ProductListedEvent event) {
        log.info("ProductListedEvent " + event);
    }

    @EventListener(ProductRestockedEvent.class)
    public void handle(ProductRestockedEvent event) {
        log.info("ProductRestockedEvent  " + event);
    }

    @EventListener(ProductSoldOutEvent.class)
    public void handle(ProductSoldOutEvent event) {
        log.info("ProductSoldOutEvent " + event);
    }

}
