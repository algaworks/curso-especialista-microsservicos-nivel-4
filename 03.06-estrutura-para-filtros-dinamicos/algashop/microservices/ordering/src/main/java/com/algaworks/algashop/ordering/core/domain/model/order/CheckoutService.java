package com.algaworks.algashop.ordering.core.domain.model.order;

import com.algaworks.algashop.ordering.core.domain.model.commons.Money;
import com.algaworks.algashop.ordering.core.domain.model.customer.Customer;
import com.algaworks.algashop.ordering.core.domain.model.shoppingcart.ShoppingCart;
import com.algaworks.algashop.ordering.core.domain.model.shoppingcart.ShoppingCartItem;
import com.algaworks.algashop.ordering.core.domain.model.shoppingcart.ShoppingCartCantProceedToCheckoutException;
import com.algaworks.algashop.ordering.core.domain.model.DomainService;
import com.algaworks.algashop.ordering.core.domain.model.product.Product;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@DomainService
@RequiredArgsConstructor
public class CheckoutService {

	private final CustomerHaveFreeShippingSpecification haveFreeShippingSpecification;

	public Order checkout(Customer customer,
						  ShoppingCart shoppingCart,
						  Billing billing,
						  Shipping shipping,
						  PaymentMethod paymentMethod,
						  CreditCardId creditCardId) {

		if (shoppingCart.isEmpty()) {
			throw new ShoppingCartCantProceedToCheckoutException();
		}

		if (shoppingCart.containsUnavailableItems()) {
			throw new ShoppingCartCantProceedToCheckoutException();
		}

		Set<ShoppingCartItem> items = shoppingCart.items();
		
		Order order = Order.draft(shoppingCart.customerId());
		order.changeBilling(billing);

		if (haveFreeShipping(customer)) {
			Shipping freeShipping = shipping.toBuilder().cost(Money.ZERO).build();
			order.changeShipping(freeShipping);
		} else {
			order.changeShipping(shipping);
		}

		order.changePaymentMethod(paymentMethod, creditCardId);

		for (ShoppingCartItem item : items) {
			order.addItem(new Product(item.productId(), item.name(),
				item.price(), item.isAvailable()), item.quantity());
		}

		order.place();
		shoppingCart.empty();

		return order;
	}

	private boolean haveFreeShipping(Customer customer) {
		return haveFreeShippingSpecification.isSatisfiedBy(customer);
	}

}
