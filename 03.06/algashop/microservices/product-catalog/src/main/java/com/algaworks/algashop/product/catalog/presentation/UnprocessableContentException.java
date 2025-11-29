package com.algaworks.algashop.product.catalog.presentation;

public class UnprocessableContentException extends RuntimeException{
    public UnprocessableContentException() {
    }

    public UnprocessableContentException(String message) {
        super(message);
    }

    public UnprocessableContentException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnprocessableContentException(Throwable cause) {
        super(cause);
    }

    public UnprocessableContentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
