package com.algaworks.algashop.ordering.core.application.customer;

import com.algaworks.algashop.ordering.core.ports.in.commons.AddressData;
import com.algaworks.algashop.ordering.core.ports.in.customer.CustomerOutput;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public class CustomerOutputTestDataBuilder {

	public static CustomerOutput.CustomerOutputBuilder existing() {
		return CustomerOutput.builder()
				.id(UUID.randomUUID())
				.registeredAt(OffsetDateTime.now())
				.phone("1191234564")
				.email("johndoe@email.com")
				.firstName("John")
				.lastName("Doe")
				.birthDate(LocalDate.of(1991, 7, 5))
				.document("12345")
				.promotionNotificationsAllowed(false)
				.loyaltyPoints(0)
				.archived(false)
				.address(AddressData.builder()
						.street("Bourbon Street")
						.number("2000")
						.complement("apt 122")
						.neighborhood("North Ville")
						.city("Yostfort")
						.state("South Carolina")
						.zipCode("12321")
						.build());
	}

}