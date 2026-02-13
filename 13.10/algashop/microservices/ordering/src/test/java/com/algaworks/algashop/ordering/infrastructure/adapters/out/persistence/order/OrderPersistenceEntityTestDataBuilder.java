package com.algaworks.algashop.ordering.infrastructure.adapters.out.persistence.order;

import com.algaworks.algashop.ordering.core.domain.model.IdGenerator;
import com.algaworks.algashop.ordering.infrastructure.adapters.out.persistence.customer.CustomerPersistenceEntityTestDataBuilder;
import com.algaworks.algashop.ordering.infrastructure.adapters.out.persistence.order.OrderPersistenceEntity.OrderPersistenceEntityBuilder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;

public class OrderPersistenceEntityTestDataBuilder {

    private OrderPersistenceEntityTestDataBuilder() {
    }

    public static OrderPersistenceEntityBuilder existingOrder() {
        return OrderPersistenceEntity.builder()
                .id(IdGenerator.generateTSID().toLong())
                .customer(CustomerPersistenceEntityTestDataBuilder.aCustomer().build())
                .totalItems(3)
                .totalAmount(new BigDecimal(1250))
                .status("DRAFT")
                .paymentMethod("CREDIT_CARD")
                .placedAt(OffsetDateTime.now())
                .items(Set.of(
                        existingItem().build(),
                        existingItemAlt().build()
                ));
    }

    public static OrderItemPersistenceEntity.OrderItemPersistenceEntityBuilder existingItem() {
        return OrderItemPersistenceEntity.builder()
                .id(IdGenerator.generateTSID().toLong())
                .price(new BigDecimal(500))
                .quantity(2)
                .totalAmount(new BigDecimal(1000))
                .productName("Notebook")
                .productId(IdGenerator.generateTimeBasedUUID());
    }

    public static OrderItemPersistenceEntity.OrderItemPersistenceEntityBuilder existingItemAlt() {
        return OrderItemPersistenceEntity.builder()
                .id(IdGenerator.generateTSID().toLong())
                .price(new BigDecimal(250))
                .quantity(1)
                .totalAmount(new BigDecimal(250))
                .productName("Mouse pad")
                .productId(IdGenerator.generateTimeBasedUUID());
    }
}
