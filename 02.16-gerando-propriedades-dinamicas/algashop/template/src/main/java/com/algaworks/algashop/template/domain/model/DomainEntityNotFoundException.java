package com.algaworks.algashop.template.domain.model;

public class DomainEntityNotFoundException extends RuntimeException {

    public DomainEntityNotFoundException() {
    }

    public DomainEntityNotFoundException(Throwable cause) {
        super(cause);
    }

    public DomainEntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public DomainEntityNotFoundException(String message) {
        super(message);
    }
}
