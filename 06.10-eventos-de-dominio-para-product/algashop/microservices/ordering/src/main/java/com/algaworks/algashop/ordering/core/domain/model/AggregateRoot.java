package com.algaworks.algashop.ordering.core.domain.model;

public interface AggregateRoot<ID> extends DomainEventSource {
    ID id();
}
