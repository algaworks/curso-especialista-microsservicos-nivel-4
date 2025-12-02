package com.algaworks.algashop.ordering.core.domain.model.customer;

import com.algaworks.algashop.ordering.core.domain.model.commons.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerTest {

    @Test
    void given_invalidEmail_whenTryCreateCustomer_shouldGenerateException() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(()-> CustomerTestDataBuilder.brandNewCustomer()
                    .email(new Email("invalid")).build()
            );
    }

    @Test
    void given_invalidEmail_whenTryUpdatedCustomerEmail_shouldGenerateException() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(()-> customer.changeEmail(new Email("invalid")));
    }

    @Test
    void given_unarchivedCustomer_whenArchive_shouldAnonymize() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();

        customer.archive();

        Assertions.assertWith(customer,
            c -> assertThat(c.fullName()).isEqualTo(new FullName("Anonymous","Anonymous")),
            c -> assertThat(c.email()).isNotEqualTo(new Email("john.doe@gmail.com")),
            c -> assertThat(c.phone()).isEqualTo(new Phone("000-000-0000")),
            c -> assertThat(c.document()).isEqualTo(new Document("000-00-0000")),
            c -> assertThat(c.birthDate()).isNull(),
            c -> assertThat(c.isPromotionNotificationsAllowed()).isFalse(),
            c -> assertThat(c.address()).isEqualTo(
                    Address.builder()
                            .street("Bourbon Street")
                            .number("Anonymized")
                            .neighborhood("North Ville")
                            .city("York")
                            .state("South California")
                            .zipCode(new ZipCode("12345"))
                            .complement(null)
                            .build()
            )
        );

    }

    @Test
    void given_archivedCustomer_whenTryToUpdate_shouldGenerateException() {
        Customer customer = CustomerTestDataBuilder.existingAnonymizedCustomer().build();

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(customer::archive);

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(()-> customer.changeEmail(new Email("email@gmail.com")));

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(()-> customer.changePhone(new Phone("123-123-1111")));

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(customer::enablePromotionNotifications);

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(customer::disablePromotionNotifications);
    }

    @Test
    void given_brandNewCustomer_whenAddLoyaltyPoints_shouldSumPoints() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();

        customer.addLoyaltyPoints(new LoyaltyPoints(10));
        customer.addLoyaltyPoints(new LoyaltyPoints(20));

        Assertions.assertThat(customer.loyaltyPoints()).isEqualTo(new LoyaltyPoints(30));
    }

    @Test
    void given_brandNewCustomer_whenAddInvalidLoyaltyPoints_shouldGenerateException() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();

        Assertions.assertThatNoException()
                .isThrownBy(()-> customer.addLoyaltyPoints(new LoyaltyPoints(0)));

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(()-> customer.addLoyaltyPoints(new LoyaltyPoints(-10)));
    }

    @Test
    void givenValidData_whenCreateBrandNewCustomer_shouldGenerateCustomerRegisteredEvent() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        CustomerRegisteredEvent event = new CustomerRegisteredEvent(customer.id(),
                customer.registeredAt(), customer.fullName(), customer.email());
        Assertions.assertThat(customer.domainEvents()).contains(event);
    }

    @Test
    void givenUnarchivedCustomer_whenArchive_shouldGenerateCustomerArchivedEvent() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().archived(false).archivedAt(null).build();
        customer.archive();
        CustomerArchivedEvent event = new CustomerArchivedEvent(customer.id(), customer.archivedAt());
        Assertions.assertThat(customer.domainEvents()).contains(event);
    }
}