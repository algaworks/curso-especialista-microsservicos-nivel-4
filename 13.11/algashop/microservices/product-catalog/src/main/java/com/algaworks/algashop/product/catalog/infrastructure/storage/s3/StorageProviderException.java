package com.algaworks.algashop.product.catalog.infrastructure.storage.s3;

public class StorageProviderException extends RuntimeException {
    public StorageProviderException() {
    }

    public StorageProviderException(String message) {
        super(message);
    }

    public StorageProviderException(String message, Throwable cause) {
        super(message, cause);
    }

    public StorageProviderException(Throwable cause) {
        super(cause);
    }

    public StorageProviderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
