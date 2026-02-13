package com.algaworks.algashop.product.catalog.domain.model.product;

import com.algaworks.algashop.product.catalog.TestcontainerMongoDBConfig;
import com.algaworks.algashop.product.catalog.infrastructure.persistence.MongoConfig;
import com.algaworks.algashop.product.catalog.infrastructure.persistence.dataload.DataLoadProperties;
import com.algaworks.algashop.product.catalog.infrastructure.persistence.dataload.DataLoader;
import com.algaworks.algashop.product.catalog.infrastructure.persistence.product.QuantityInStockAdjustmentMongoDBImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.data.mongodb.test.autoconfigure.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@DataMongoTest
@Import({
        MongoConfig.class,
        QuantityInStockAdjustmentMongoDBImpl.class,
        DataLoader.class,
        DataLoadProperties.class,
        TestcontainerMongoDBConfig.class
})
//@Transactional
class QuantityInStockAdjustmentIT {

    @Autowired
    private QuantityInStockAdjustment quantityInStockAdjustment;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DataLoader dataLoader;

    private static UUID existingProduct = UUID.fromString("946cea3b-d11d-4f11-b88d-3089b4e74087");

    @BeforeEach
    public void beforeEach() throws Exception {
        dataLoader.run(new DefaultApplicationArguments());
    }

    @Test
    public void shouldIncreaseQuantity() {
        Product product = productRepository.findById(existingProduct).orElseThrow();

        quantityInStockAdjustment.increase(existingProduct, 25);
        quantityInStockAdjustment.increase(existingProduct, 25);

        Product productUpdated = productRepository.findById(existingProduct).orElseThrow();

        Assertions.assertThat(product.getQuantityInStock()).isEqualTo(50);
        Assertions.assertThat(productUpdated.getQuantityInStock()).isEqualTo(100);
    }

    @Test
    public void shouldDecreaseQuantity() {
        Product product = productRepository.findById(existingProduct).orElseThrow();

        quantityInStockAdjustment.decrease(existingProduct, 25);
        quantityInStockAdjustment.decrease(existingProduct, 25);

        Product productUpdated = productRepository.findById(existingProduct).orElseThrow();

        Assertions.assertThat(product.getQuantityInStock()).isEqualTo(50);
        Assertions.assertThat(productUpdated.getQuantityInStock()).isEqualTo(0);
    }

    @Test
    public void shouldNotDecreaseQuantity() {
        Assertions.assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(()-> quantityInStockAdjustment.decrease(existingProduct, 100));
        Product product = productRepository.findById(existingProduct).orElseThrow();
        Assertions.assertThat(product.getQuantityInStock()).isEqualTo(50);
    }

    @Test
    public void shouldCalculateResult() {
        Product product = productRepository.findById(existingProduct).orElseThrow();
        var result = quantityInStockAdjustment.decrease(product.getId(), 40);

        Assertions.assertThat(result.newQuantity()).isEqualTo(10);
        Assertions.assertThat(result.previousQuantity()).isEqualTo(50);
    }

}