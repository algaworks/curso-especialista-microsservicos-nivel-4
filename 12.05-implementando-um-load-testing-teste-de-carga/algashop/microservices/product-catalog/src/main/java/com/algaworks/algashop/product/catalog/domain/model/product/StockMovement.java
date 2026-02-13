package com.algaworks.algashop.product.catalog.domain.model.product;

import com.algaworks.algashop.product.catalog.domain.model.IdGenerator;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.OffsetDateTime;
import java.util.UUID;

@Document(collection = "stock_movements")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class StockMovement {
    @Id
    @EqualsAndHashCode.Include
    private UUID id;
    private OffsetDateTime occurredAt;
    private UUID productId;
    private Integer movementQuantity;
    private Integer previousQuantity;
    private Integer newQuantity;
    private MovementType type;

    @Builder
    public StockMovement(UUID productId,
                         Integer movementQuantity,
                         Integer previousQuantity,
                         Integer newQuantity,
                         MovementType type) {
        this.id = IdGenerator.generateTimeBasedUUID();
        this.occurredAt = OffsetDateTime.now();

        this.productId = productId;
        this.movementQuantity = movementQuantity;
        this.previousQuantity = previousQuantity;
        this.newQuantity = newQuantity;
        this.type = type;
    }

    public enum MovementType {
        STOCK_IN,
        STOCK_OUT
    }
}
