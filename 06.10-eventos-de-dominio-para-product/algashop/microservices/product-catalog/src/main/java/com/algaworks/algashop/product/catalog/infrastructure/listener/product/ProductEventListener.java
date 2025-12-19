package com.algaworks.algashop.product.catalog.infrastructure.listener.product;

import com.algaworks.algashop.product.catalog.domain.model.product.ProductPlacedOnSaleEvent;
import com.algaworks.algashop.product.catalog.domain.model.product.ProductPriceChangedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProductEventListener {
    
    @EventListener(ProductPriceChangedEvent.class)
    public void handle(ProductPriceChangedEvent event) {
        log.info("ProductPriceChangedEvent " + event);
    }

    @EventListener(ProductPlacedOnSaleEvent.class)
    public void handle(ProductPlacedOnSaleEvent event) {
        log.info("ProductPlacedOnSaleEvent " + event);
    }

}
