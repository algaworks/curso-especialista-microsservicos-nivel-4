package com.algaworks.algashop.ordering.infrastructure.adapters.out.persistence.customer;

import com.algaworks.algashop.ordering.core.domain.model.customer.Customer;
import com.algaworks.algashop.ordering.core.domain.model.commons.Address;
import com.algaworks.algashop.ordering.infrastructure.adapters.out.persistence.commons.AddressEmbeddable;
import org.springframework.stereotype.Component;

@Component
public class CustomerPersistenceEntityAssembler {

	public CustomerPersistenceEntity fromDomain(Customer customer) {
		return merge(new CustomerPersistenceEntity(), customer);
	}

	public CustomerPersistenceEntity merge(CustomerPersistenceEntity customerPersistenceEntity, Customer customer) {
		customerPersistenceEntity.setId(customer.id().value());
		customerPersistenceEntity.setFirstName(customer.fullName().firstName());
		customerPersistenceEntity.setLastName(customer.fullName().lastName());
		customerPersistenceEntity.setBirthDate(customer.birthDate() != null ? customer.birthDate().value() : null);
		customerPersistenceEntity.setEmail(customer.email().value());
		customerPersistenceEntity.setPhone(customer.phone().value());
		customerPersistenceEntity.setDocument(customer.document().value());
		customerPersistenceEntity.setPromotionNotificationsAllowed(customer.isPromotionNotificationsAllowed());
		customerPersistenceEntity.setArchived(customer.isArchived());
		customerPersistenceEntity.setRegisteredAt(customer.registeredAt());
		customerPersistenceEntity.setArchivedAt(customer.registeredAt());
		customerPersistenceEntity.setLoyaltyPoints(customer.loyaltyPoints().value());
		customerPersistenceEntity.setAddress(toAddressEmbeddable(customer.address()));
		customerPersistenceEntity.setVersion(customer.version());
		customerPersistenceEntity.addEvents(customer.domainEvents());
		return customerPersistenceEntity;
	}

	private AddressEmbeddable toAddressEmbeddable(Address address) {
		return AddressEmbeddable.builder()
				.city(address.city())
				.state(address.state())
				.number(address.number())
				.street(address.street())
				.complement(address.complement())
				.neighborhood(address.neighborhood())
				.zipCode(address.zipCode().value())
				.build();
	}
}