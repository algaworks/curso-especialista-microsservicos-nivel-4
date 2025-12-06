package com.algaworks.algashop.ordering.infrastructure.adapters.out.persistence.customer;

import com.algaworks.algashop.ordering.core.domain.model.customer.Customer;
import com.algaworks.algashop.ordering.core.domain.model.customer.Customers;
import com.algaworks.algashop.ordering.core.domain.model.commons.Email;
import com.algaworks.algashop.ordering.core.domain.model.customer.CustomerId;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomersPersistenceProvider implements Customers {

	private final CustomerPersistenceEntityRepository persistenceRepository;
	private final CustomerPersistenceEntityAssembler assembler;
	private final CustomerPersistenceEntityDisassembler disassembler;

	private final EntityManager entityManager;

	@Override
	public Optional<Customer> ofId(CustomerId customerId) {
		return persistenceRepository.findById(customerId.value())
				.map(disassembler::toDomainEntity);
	}

	@Override
	public boolean exists(CustomerId customerId) {
		return persistenceRepository.existsById(customerId.value());
	}

	@Override
	@Transactional(readOnly = false)
	public void add(Customer aggregateRoot) {
		UUID customerId = aggregateRoot.id().value();

		persistenceRepository.findById(customerId)
				.ifPresentOrElse(
						(persistenceEntity) -> update(aggregateRoot, persistenceEntity),
						()-> insert(aggregateRoot)
				);

		aggregateRoot.clearDomainEvents();
	}

	@Override
	public long count() {
		return persistenceRepository.count();
	}

	@Override
	public Optional<Customer> ofEmail(Email email) {
		return persistenceRepository.findByEmail(email.value())
				.map(disassembler::toDomainEntity);
	}

	@Override
	public boolean isEmailUnique(Email email, CustomerId exceptCustomerId) {
		return !persistenceRepository.existsByEmailAndIdNot(email.value(), exceptCustomerId.value());
	}

	private void update(Customer aggregateRoot, CustomerPersistenceEntity persistenceEntity) {
		persistenceEntity = assembler.merge(persistenceEntity, aggregateRoot);
		entityManager.detach(persistenceEntity);
		persistenceEntity = persistenceRepository.saveAndFlush(persistenceEntity);
		updateVersion(aggregateRoot, persistenceEntity);
	}

	private void insert(Customer aggregateRoot) {
		CustomerPersistenceEntity persistenceEntity = assembler.fromDomain(aggregateRoot);
		persistenceRepository.saveAndFlush(persistenceEntity);
		updateVersion(aggregateRoot, persistenceEntity);
	}

	@SneakyThrows
	private void updateVersion(Customer aggregateRoot, CustomerPersistenceEntity persistenceEntity) {
		Field version = aggregateRoot.getClass().getDeclaredField("version");
		version.setAccessible(true);
		ReflectionUtils.setField(version, aggregateRoot, persistenceEntity.getVersion());
		version.setAccessible(false);
	}

}