package com.algaworks.algashop.ordering.infrastructure.adapters.out.persistence.shoppingcart;

import com.algaworks.algashop.ordering.core.domain.model.commons.Money;
import com.algaworks.algashop.ordering.core.domain.model.commons.Quantity;
import com.algaworks.algashop.ordering.core.domain.model.customer.CustomerTestDataBuilder;
import com.algaworks.algashop.ordering.core.domain.model.product.Product;
import com.algaworks.algashop.ordering.core.domain.model.product.ProductId;
import com.algaworks.algashop.ordering.core.domain.model.product.ProductTestDataBuilder;
import com.algaworks.algashop.ordering.core.domain.model.shoppingcart.ShoppingCart;
import com.algaworks.algashop.ordering.core.domain.model.shoppingcart.ShoppingCartItem;
import com.algaworks.algashop.ordering.core.domain.model.shoppingcart.ShoppingCartTestDataBuilder;
import com.algaworks.algashop.ordering.infrastructure.adapters.out.persistence.AbstractPersistenceIT;
import com.algaworks.algashop.ordering.infrastructure.adapters.out.persistence.customer.CustomerPersistenceEntityAssembler;
import com.algaworks.algashop.ordering.infrastructure.adapters.out.persistence.customer.CustomerPersistenceEntityDisassembler;
import com.algaworks.algashop.ordering.infrastructure.adapters.out.persistence.customer.CustomersPersistenceProvider;
import com.algaworks.algashop.ordering.infrastructure.config.auditing.SpringDataAuditingConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Import({
        ShoppingCartUpdateProvider.class,
        ShoppingCartsPersistenceProvider.class,
        ShoppingCartPersistenceEntityAssembler.class,
        ShoppingCartPersistenceEntityDisassembler.class,
        CustomersPersistenceProvider.class,
        CustomerPersistenceEntityAssembler.class,
        CustomerPersistenceEntityDisassembler.class,
        SpringDataAuditingConfig.class
})
@Sql(scripts = "classpath:db/clean/afterMigrate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class ShoppingCartUpdateProviderIT extends AbstractPersistenceIT {

    private ShoppingCartsPersistenceProvider persistenceProvider;
    private CustomersPersistenceProvider customersPersistenceProvider;
    private ShoppingCartPersistenceEntityRepository entityRepository;

    private ShoppingCartUpdateProvider shoppingCartUpdateProvider;

    @Autowired
    public ShoppingCartUpdateProviderIT(ShoppingCartsPersistenceProvider persistenceProvider,
                                        CustomersPersistenceProvider customersPersistenceProvider,
                                        ShoppingCartPersistenceEntityRepository entityRepository,
                                        ShoppingCartUpdateProvider shoppingCartUpdateProvider) {
        this.persistenceProvider = persistenceProvider;
        this.customersPersistenceProvider = customersPersistenceProvider;
        this.entityRepository = entityRepository;
        this.shoppingCartUpdateProvider = shoppingCartUpdateProvider;
    }

    @BeforeEach
    public void setup() {
        if (!customersPersistenceProvider.exists(CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID)) {
            customersPersistenceProvider.add(
                    CustomerTestDataBuilder.existingCustomer().build()
            );
        }
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void shouldUpdateItemPriceAndTotalAmount() {
        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.aShoppingCart().withItems(false).build();

        Product product1 = ProductTestDataBuilder.aProduct().price(new Money("2000")).build();
        Product product2 = ProductTestDataBuilder.aProductAltRamMemory().price(new Money("200")).build();

        shoppingCart.addItem(product1, new Quantity(2));
        shoppingCart.addItem(product2, new Quantity(1));

        persistenceProvider.add(shoppingCart);

        ProductId productIdToUpdate = product1.id();
        Money newProduct1Price = new Money("1500");
        Money expectedNewItemTotalPrice = newProduct1Price.multiply(new Quantity(2));
        Money expectedNewCartTotalAmount = expectedNewItemTotalPrice.add(new Money("200"));

        shoppingCartUpdateProvider.adjustPrice(productIdToUpdate, newProduct1Price);

        ShoppingCart updatedShoppingCart = persistenceProvider.ofId(shoppingCart.id()).orElseThrow();

        Assertions.assertThat(updatedShoppingCart.totalAmount()).isEqualTo(expectedNewCartTotalAmount);
        Assertions.assertThat(updatedShoppingCart.totalItems()).isEqualTo(new Quantity(3));

        ShoppingCartItem item = updatedShoppingCart.findItem(productIdToUpdate);

        Assertions.assertThat(item.totalAmount()).isEqualTo(expectedNewItemTotalPrice);
        Assertions.assertThat(item.price()).isEqualTo(newProduct1Price);

    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void shouldUpdateItemAvailability() {
        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.aShoppingCart().withItems(false).build();

        Product product1 = ProductTestDataBuilder.aProduct()
                .price(new Money("2000"))
                .inStock(true).build();
        Product product2 = ProductTestDataBuilder.aProductAltRamMemory()
                .price(new Money("200"))
                .inStock(true).build();

        shoppingCart.addItem(product1, new Quantity(2));
        shoppingCart.addItem(product2, new Quantity(1));

        persistenceProvider.add(shoppingCart);

        var productIdToUpdate = product1.id();
        var productIdNotToUpdate = product2.id();

        shoppingCartUpdateProvider.changeAvailability(productIdToUpdate, false);

        ShoppingCart updatedShoppingCart = persistenceProvider.ofId(shoppingCart.id()).orElseThrow();

        ShoppingCartItem item = updatedShoppingCart.findItem(productIdToUpdate);

        Assertions.assertThat(item.isAvailable()).isFalse();

        ShoppingCartItem item2 = updatedShoppingCart.findItem(productIdNotToUpdate);

        Assertions.assertThat(item2.isAvailable()).isTrue();

    }


}