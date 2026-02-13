package com.algaworks.algashop.ordering.core.domain.model.shoppingcart;

import com.algaworks.algashop.ordering.core.domain.model.commons.Money;
import com.algaworks.algashop.ordering.core.domain.model.commons.Quantity;
import com.algaworks.algashop.ordering.core.domain.model.customer.CustomerId;
import com.algaworks.algashop.ordering.core.domain.model.product.Product;
import com.algaworks.algashop.ordering.core.domain.model.product.ProductTestDataBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

class ShoppingCartTest {

    @Test
    void givenCustomer_whenStartShopping_shouldInitializeEmptyCart() {
        var customerId = new CustomerId();

        ShoppingCart cart = ShoppingCart.startShopping(customerId);

        Assertions.assertWith(cart,
                c -> Assertions.assertThat(c.id()).isNotNull(),
                c -> Assertions.assertThat(c.customerId()).isEqualTo(customerId),
                c -> Assertions.assertThat(c.totalAmount()).isEqualTo(Money.ZERO),
                c -> Assertions.assertThat(c.totalItems()).isEqualTo(Quantity.ZERO),
                c -> Assertions.assertThat(c.isEmpty()).isTrue(),
                c -> Assertions.assertThat(c.items()).isEmpty()
        );
    }

    @Test
    void givenEmptyCart_whenAddNewItem_shouldContainItemAndRecalculateTotals() {
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().withItems(false).build();
        Product product = ProductTestDataBuilder.aProduct().build();

        cart.addItem(product, new Quantity(2));

        Assertions.assertThat(cart.items()).hasSize(1);
        var item = cart.items().iterator().next();
        Assertions.assertThat(item.productId()).isEqualTo(product.id());
        Assertions.assertThat(item.quantity()).isEqualTo(new Quantity(2));
        Assertions.assertThat(cart.totalItems()).isEqualTo(new Quantity(2));
        Assertions.assertThat(cart.totalAmount()).isEqualTo(
                new Money(product.price().value().multiply(new BigDecimal(2))));
    }

    @Test
    void givenCartWithExistingProduct_whenAddSameProduct_shouldIncrementQuantity() {
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().withItems(false).build();
        Product product = ProductTestDataBuilder.aProduct().build();

        cart.addItem(product, new Quantity(3));
        cart.addItem(product, new Quantity(3));
        var existing = cart.items().iterator().next();

        Set<ShoppingCartItem> items = cart.items();
        Assertions.assertThat(items).hasSize(1);
        Assertions.assertThat(existing.quantity()).isEqualTo(new Quantity(6));
    }

    @Test
    void givenCartWithItems_whenRemoveExistingItem_shouldRemoveAndRecalculateTotals() {
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        var item = cart.items().iterator().next();

        cart.removeItem(item.id());

        Assertions.assertThat(cart.items()).doesNotContain(item);
        Assertions.assertThat(cart.totalItems()).isEqualTo(
                new Quantity(cart.items().stream().mapToInt(i -> i.quantity().value()).sum())
        );
    }

    @Test
    void givenCartWithItems_whenRemoveNonexistentItem_shouldThrowShoppingCartDoesNotContainItemException() {
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        ShoppingCartItemId randomId = new ShoppingCartItemId();

        Assertions.assertThatExceptionOfType(ShoppingCartDoesNotContainItemException.class)
                .isThrownBy(() -> cart.removeItem(randomId));
    }

    @Test
    void givenCartWithItems_whenEmpty_shouldClearAllItemsAndResetTotals() {
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();

        cart.empty();

        Assertions.assertWith(cart,
                c -> Assertions.assertThat(c.isEmpty()).isTrue(),
                c -> Assertions.assertThat(c.totalItems()).isEqualTo(Quantity.ZERO),
                c -> Assertions.assertThat(c.totalAmount()).isEqualTo(Money.ZERO)
        );
    }

    @Test
    void givenCartWithItems_whenChangeItemPrice_shouldRecalculateTotalAmount() {
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().withItems(false).build();


        Product product = ProductTestDataBuilder.aProduct()
                .build();

        cart.addItem(product, new Quantity(2));

        product = ProductTestDataBuilder.aProduct()
                .price(new Money("100"))
                .build();
        cart.refreshItem(product);

        var item = cart.findItem(product.id());

        Assertions.assertThat(item.price()).isEqualTo(new Money("100"));
        Assertions.assertThat(cart.totalAmount()).isEqualTo(new Money("200"));
    }

    @Test
    void givenCartWithItems_whenDetectUnavailableItems_shouldReturnTrue() {
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        Product product = ProductTestDataBuilder.aProduct().inStock(false).build();
        cart.refreshItem(product);

        Assertions.assertThat(cart.containsUnavailableItems()).isTrue();
    }

    @Test
    void givenCartWithItems_whenChangeQuantityToZero_shouldThrowIllegalArgumentException() {
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        var item = cart.items().iterator().next();

        Assertions.assertThatIllegalArgumentException()
                .isThrownBy(() -> cart.changeItemQuantity(item.id(), Quantity.ZERO));
    }

    @Test
    void givenCartWithItems_whenChangeItemQuantity_shouldRecalculateTotalItems() {
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        var item = cart.items().iterator().next();

        cart.changeItemQuantity(item.id(), new Quantity(5));

        Assertions.assertThat(cart.totalItems()).isEqualTo(
                new Quantity(cart.items().stream().mapToInt(i -> i.quantity().value()).sum())
        );
    }

    @Test
    void givenCartWithItems_whenFindItemById_shouldReturnItem() {
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        var item = cart.items().iterator().next();

        var found = cart.findItem(item.id());

        Assertions.assertThat(found).isEqualTo(item);
    }

    @Test
    public void givenDifferentIds_whenCompareItems_shouldNotBeEqual() {
        ShoppingCart shoppingCart1 = ShoppingCartTestDataBuilder.aShoppingCart().build();
        ShoppingCart shoppingCart2 = ShoppingCartTestDataBuilder.aShoppingCart().build();

        Assertions.assertThat(shoppingCart1).isNotEqualTo(shoppingCart2);
    }
}
