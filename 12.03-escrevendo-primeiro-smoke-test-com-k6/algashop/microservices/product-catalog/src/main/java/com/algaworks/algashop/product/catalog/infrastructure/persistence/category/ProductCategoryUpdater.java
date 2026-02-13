package com.algaworks.algashop.product.catalog.infrastructure.persistence.category;

import com.algaworks.algashop.product.catalog.application.category.event.CategoryUpdatedEvent;
import com.algaworks.algashop.product.catalog.domain.model.product.Product;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProductCategoryUpdater {

    private final MongoOperations mongoOperations;

    public void copyCategoryDataToProducts(CategoryUpdatedEvent categoryUpdatedEvent) {
        Query query = new Query(
                Criteria.where("category._id").is(categoryUpdatedEvent.getCategoryId())
        );

        Update update = new Update()
                .set("category.name", categoryUpdatedEvent.getName())
                .set("category.enabled", categoryUpdatedEvent.getEnabled());

        mongoOperations.updateMulti(query, update, Product.class);
    }

}
