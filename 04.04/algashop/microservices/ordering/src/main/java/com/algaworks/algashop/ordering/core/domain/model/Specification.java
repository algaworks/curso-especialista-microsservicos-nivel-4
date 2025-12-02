package com.algaworks.algashop.ordering.core.domain.model;

public interface Specification<T> {
    boolean isSatisfiedBy(T t);

    default Specification<T> and(Specification<T> other) {
        return t -> this.isSatisfiedBy(t) && other.isSatisfiedBy(t);
    }

    default Specification<T> or(Specification<T> other) {
        return t -> this.isSatisfiedBy(t) || other.isSatisfiedBy(t);
    }

    default Specification<T> not() {
        return t -> !this.isSatisfiedBy(t);
    }

    default Specification<T> andNot(Specification<T> other) {
        return t -> this.isSatisfiedBy(t) && !other.isSatisfiedBy(t);
    }

}
