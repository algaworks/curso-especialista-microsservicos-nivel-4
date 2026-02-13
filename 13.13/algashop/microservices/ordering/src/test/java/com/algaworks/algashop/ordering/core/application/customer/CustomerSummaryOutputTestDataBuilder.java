package com.algaworks.algashop.ordering.core.application.customer;

import com.algaworks.algashop.ordering.core.ports.in.customer.CustomerSummaryOutput;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public class CustomerSummaryOutputTestDataBuilder {

	public static CustomerSummaryOutput.CustomerSummaryOutputBuilder existing() {
		return CustomerSummaryOutput.builder()
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
				.archived(false);
	}

	public static CustomerSummaryOutput.CustomerSummaryOutputBuilder existingAlt1() {
		return CustomerSummaryOutput.builder()
				.id(UUID.randomUUID())
				.registeredAt(OffsetDateTime.now())
				.phone("119123456")
				.email("scott1977@email.com")
				.firstName("Scott")
				.lastName("Stacey")
				.birthDate(LocalDate.of(1977, 1, 5))
				.document("98745")
				.promotionNotificationsAllowed(true)
				.loyaltyPoints(10)
				.archived(false);
	}

}