package com.algaworks.algashop.ordering.core.domain.model.customer;

import com.algaworks.algashop.ordering.core.domain.model.AbstractDomainIT;
import com.algaworks.algashop.ordering.core.domain.model.commons.Email;
import com.algaworks.algashop.ordering.core.domain.model.commons.FullName;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CustomersIT extends AbstractDomainIT {

    private Customers customers;

    @Autowired
    public CustomersIT(Customers customers) {
        this.customers = customers;
    }

    @Test
    public void shouldPersistAndFind() {
        Customer originalCustomer = CustomerTestDataBuilder.brandNewCustomer().build();
        CustomerId customerId = originalCustomer.id();
        customers.add(originalCustomer);

        Optional<Customer> possibleCustomer = customers.ofId(customerId);

        assertThat(possibleCustomer).isPresent();

        Customer savedCustomer = possibleCustomer.get();

        assertThat(savedCustomer).satisfies(
                s -> assertThat(s.id()).isEqualTo(customerId)
        );
    }

    @Test
    public void shouldUpdateExistingCustomer() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);

        customer = customers.ofId(customer.id()).orElseThrow();
        customer.archive();

        customers.add(customer);

        Customer savedCustomer = customers.ofId(customer.id()).orElseThrow();

        Assertions.assertThat(savedCustomer.archivedAt()).isNotNull();
        Assertions.assertThat(savedCustomer.isArchived()).isTrue();

    }

    @Test
    public void shouldNotAllowStaleUpdates() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);

        Customer customerT1 = customers.ofId(customer.id()).orElseThrow();
        Customer customerT2 = customers.ofId(customer.id()).orElseThrow();

        customerT1.archive();
        customers.add(customerT1);

        customerT2.changeName(new FullName("Alex","Silva"));

        Assertions.assertThatExceptionOfType(ObjectOptimisticLockingFailureException.class)
                .isThrownBy(()-> customers.add(customerT2));

        Customer savedCustomer = customers.ofId(customer.id()).orElseThrow();

        Assertions.assertThat(savedCustomer.archivedAt()).isNotNull();
        Assertions.assertThat(savedCustomer.isArchived()).isTrue();

    }

    @Test
    public void shouldCountExistingOrders() {
        Assertions.assertThat(customers.count()).isZero();

        Customer customer1 = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer1);

        Customer customer2 = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer2);

        Assertions.assertThat(customers.count()).isEqualTo(2L);
    }

    @Test
    public void shouldReturnValidateIfOrderExists() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);

        Assertions.assertThat(customers.exists(customer.id())).isTrue();
        Assertions.assertThat(customers.exists(new CustomerId())).isFalse();
    }

    @Test
    public void shouldFindByEmail() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);

        Optional<Customer> customerOptional = customers.ofEmail(customer.email());

        Assertions.assertThat(customerOptional).isPresent();
    }

    @Test
    public void shouldReturnIfEmailIsInUse() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);

        Assertions.assertThat(customers.isEmailUnique(customer.email(), customer.id())).isTrue();
        Assertions.assertThat(customers.isEmailUnique(customer.email(), new CustomerId())).isFalse();
        Assertions.assertThat(customers.isEmailUnique(new Email("alex@gmail.com"), new CustomerId())).isTrue();
    }

 }