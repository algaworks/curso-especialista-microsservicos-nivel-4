package com.algaworks.algashop.ordering.core.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class AbstractEventSourceEntity implements DomainEventSource {

    protected final List<Object> domainEvents = new ArrayList<>();

    protected void publishDomainEvent(Object event) {
        Objects.requireNonNull(event);
        this.domainEvents.add(event);
    }

    @Override
    public List<Object> domainEvents() {
        return Collections.unmodifiableList(this.domainEvents);
    }

    @Override
    public void clearDomainEvents() {
        this.domainEvents.clear();
    }

}
