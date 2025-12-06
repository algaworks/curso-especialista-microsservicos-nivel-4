package com.algaworks.algashop.ordering.core.domain.model.shoppingcart;

import com.algaworks.algashop.ordering.core.domain.model.customer.CustomerAlreadyHaveShoppingCartException;
import com.algaworks.algashop.ordering.core.domain.model.customer.CustomerNotFoundException;
import com.algaworks.algashop.ordering.core.domain.model.customer.Customers;
import com.algaworks.algashop.ordering.core.domain.model.DomainService;
import com.algaworks.algashop.ordering.core.domain.model.customer.CustomerId;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class ShoppingService {
	
	private final ShoppingCarts shoppingCarts;
	private final Customers customers;

	public ShoppingCart startShopping(CustomerId customerId) {
		if (!customers.exists(customerId)) {
			throw new CustomerNotFoundException(customerId);
		}

		if (shoppingCarts.ofCustomer(customerId).isPresent()) {
			throw new CustomerAlreadyHaveShoppingCartException(customerId);
		}

		return ShoppingCart.startShopping(customerId);
	}

}