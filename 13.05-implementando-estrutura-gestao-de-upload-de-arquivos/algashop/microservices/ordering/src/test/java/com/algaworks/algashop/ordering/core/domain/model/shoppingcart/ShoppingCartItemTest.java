package com.algaworks.algashop.ordering.core.domain.model.shoppingcart;

import com.algaworks.algashop.ordering.core.domain.model.commons.Money;
import com.algaworks.algashop.ordering.core.domain.model.commons.Quantity;
import com.algaworks.algashop.ordering.core.domain.model.product.Product;
import com.algaworks.algashop.ordering.core.domain.model.product.ProductId;
import com.algaworks.algashop.ordering.core.domain.model.product.ProductName;
import com.algaworks.algashop.ordering.core.domain.model.product.ProductTestDataBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ShoppingCartItemTest {

    @Test
    public void givenValidData_whenCreateNewItem_shouldInitializeCorrectly() {
        ShoppingCartItem item = ShoppingCartItemTestDataBuilder.aShoppingCartItem()
                .productName(new ProductName("Notebook"))
                .price(new Money("2000"))
                .quantity(new Quantity(2))
                .available(true)
                .build();

        Assertions.assertWith(item,
                i -> Assertions.assertThat(i.id()).isNotNull(),
                i -> Assertions.assertThat(i.shoppingCartId()).isNotNull(),
                i -> Assertions.assertThat(i.productId()).isNotNull(),
                i -> Assertions.assertThat(i.name()).isEqualTo(new ProductName("Notebook")),
                i -> Assertions.assertThat(i.price()).isEqualTo(new Money("2000")),
                i -> Assertions.assertThat(i.quantity()).isEqualTo(new Quantity(2)),
                i -> Assertions.assertThat(i.isAvailable()).isTrue(),
                i -> Assertions.assertThat(i.totalAmount()).isEqualTo(new Money("4000"))
        );
    }

    @Test
    public void givenItem_whenChangeQuantity_shouldRecalculateTotal() {
        ShoppingCartItem item = ShoppingCartItemTestDataBuilder.aShoppingCartItem()
                .price(new Money("1000"))
                .quantity(new Quantity(1))
                .build();

        item.changeQuantity(new Quantity(3));

        Assertions.assertWith(item,
                i -> Assertions.assertThat(i.quantity()).isEqualTo(new Quantity(3)),
                i -> Assertions.assertThat(i.totalAmount()).isEqualTo(new Money("3000"))
        );
    }

    @Test
    public void givenItem_whenChangePrice_shouldRecalculateTotal() {
        ShoppingCartItem item = ShoppingCartItemTestDataBuilder.aShoppingCartItem()
                .price(new Money("1500"))
                .quantity(new Quantity(2))
                .build();

        Product product = ProductTestDataBuilder.aProduct().build();
        item.refresh(product);

        Assertions.assertWith(item,
                i -> Assertions.assertThat(i.price()).isEqualTo(product.price()),
                i -> Assertions.assertThat(i.totalAmount()).isEqualTo(product.price().multiply(new Quantity(2)))
        );
    }

    @Test
    public void givenItem_whenChangeAvailability_shouldUpdateStatus() {
        ShoppingCartItem item = ShoppingCartItemTestDataBuilder.aShoppingCartItem()
                .available(true)
                .build();

        Product product = ProductTestDataBuilder.aProduct()
                .inStock(false)
                .build();

        item.refresh(product);

        Assertions.assertThat(item.isAvailable()).isFalse();
    }

    @Test
    public void givenEqualIds_whenCompareItems_shouldBeEqual() {
        ShoppingCartId cartId = new ShoppingCartId();
        ProductId productId = new ProductId();
        ShoppingCartItemId shoppingCartItemId = new ShoppingCartItemId();

        ShoppingCartItem item1 = ShoppingCartItem.existing()
                .id(shoppingCartItemId)
                .shoppingCartId(cartId)
                .productId(productId)
                .productName(new ProductName("Mouse"))
                .price(new Money("100"))
                .quantity(new Quantity(1))
                .available(true)
                .totalAmount(new Money("100"))
                .build();

        ShoppingCartItem item2 = ShoppingCartItem.existing()
                .id(shoppingCartItemId)
                .shoppingCartId(cartId)
                .productId(productId)
                .productName(new ProductName("Notebook"))
                .price(new Money("100"))
                .quantity(new Quantity(1))
                .available(true)
                .totalAmount(new Money("100"))
                .build();

        Assertions.assertThat(item1).isEqualTo(item2);
        Assertions.assertThat(item1.hashCode()).isEqualTo(item2.hashCode());
    }

    @Test
    public void givenDifferentIds_whenCompareItems_shouldNotBeEqual() {
        ShoppingCartItem item1 = ShoppingCartItemTestDataBuilder.aShoppingCartItem().build();
        ShoppingCartItem item2 = ShoppingCartItemTestDataBuilder.aShoppingCartItem().build();

        Assertions.assertThat(item1).isNotEqualTo(item2);
    }
}
