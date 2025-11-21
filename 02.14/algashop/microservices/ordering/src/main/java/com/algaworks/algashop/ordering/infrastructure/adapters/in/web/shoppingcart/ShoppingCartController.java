package com.algaworks.algashop.ordering.infrastructure.adapters.in.web.shoppingcart;

import com.algaworks.algashop.ordering.core.domain.model.customer.CustomerNotFoundException;
import com.algaworks.algashop.ordering.core.domain.model.product.ProductNotFoundException;
import com.algaworks.algashop.ordering.core.ports.in.shoppingcart.ForManagingShoppingCarts;
import com.algaworks.algashop.ordering.core.ports.in.shoppingcart.ForQueryingShoppingCarts;
import com.algaworks.algashop.ordering.core.ports.in.shoppingcart.ShoppingCartItemInput;
import com.algaworks.algashop.ordering.core.ports.in.shoppingcart.ShoppingCartOutput;
import com.algaworks.algashop.ordering.infrastructure.adapters.in.web.exceptionhandler.UnprocessableEntityException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-carts")
@RequiredArgsConstructor
public class ShoppingCartController {

	private final ForManagingShoppingCarts forManagingShoppingCarts;
	private final ForQueryingShoppingCarts forQueryingShoppingCarts;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ShoppingCartOutput create(@RequestBody @Valid ShoppingCartInput input) {
		UUID shoppingCartId;
        try {
			shoppingCartId = forManagingShoppingCarts.createNew(input.getCustomerId());
		} catch (CustomerNotFoundException e) {
			throw new UnprocessableEntityException(e.getMessage(), e);
		}
        return forQueryingShoppingCarts.findById(shoppingCartId);
	}

	@GetMapping("/{shoppingCartId}")
	public ShoppingCartOutput getById(@PathVariable UUID shoppingCartId) {
		return forQueryingShoppingCarts.findById(shoppingCartId);
	}

	@GetMapping("/{shoppingCartId}/items")
	public ShoppingCartItemListModel getItems(@PathVariable UUID shoppingCartId) {
		var items = forQueryingShoppingCarts.findById(shoppingCartId).getItems();
		return new ShoppingCartItemListModel(items);
	}

	@DeleteMapping("/{shoppingCartId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable UUID shoppingCartId) {
		forManagingShoppingCarts.delete(shoppingCartId);
	}

	@DeleteMapping("/{shoppingCartId}/items")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void empty(@PathVariable UUID shoppingCartId) {
		forManagingShoppingCarts.empty(shoppingCartId);
	}

	@PostMapping("/{shoppingCartId}/items")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void addItem(@PathVariable UUID shoppingCartId,
		   			    @RequestBody @Valid ShoppingCartItemInput input) {
		input.setShoppingCartId(shoppingCartId);
		try {
			forManagingShoppingCarts.addItem(input);
		} catch (ProductNotFoundException e) {
			throw new UnprocessableEntityException(e.getMessage(), e);
		}
	}

	@DeleteMapping("/{shoppingCartId}/items/{itemId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void removeItem(@PathVariable UUID shoppingCartId,
						   @PathVariable UUID itemId) {
		forManagingShoppingCarts.removeItem(shoppingCartId, itemId);
	}
}