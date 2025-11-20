package com.algaworks.algashop.ordering.infrastructure.adapters.out.persistence.customer;

import com.algaworks.algashop.ordering.infrastructure.adapters.out.persistence.commons.AddressEmbeddable;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import static com.algaworks.algashop.ordering.core.domain.model.customer.CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID;

public class CustomerPersistenceEntityTestDataBuilder {

    private CustomerPersistenceEntityTestDataBuilder() {

    }

    public static CustomerPersistenceEntity.CustomerPersistenceEntityBuilder aCustomer() {
        return CustomerPersistenceEntity.builder()
                .id(DEFAULT_CUSTOMER_ID.value())
                .registeredAt(OffsetDateTime.now())
                .promotionNotificationsAllowed(true)
                .archived(false)
                .archivedAt(null)
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1991, 7,5))
                .email("johndoe@email.com")
                .phone("478-256-2604")
                .document("255-08-0578")
                .promotionNotificationsAllowed(true)
                .loyaltyPoints(0)
                .address(AddressEmbeddable.builder()
                        .street("Bourbon Street")
                        .number("1134")
                        .neighborhood("North Ville")
                        .city("York")
                        .state("South California")
                        .zipCode("12345")
                        .complement("Apt. 114")
                        .build())
                ;
    }
}
