package com.algaworks.algashop.product.catalog.infrastructure.persistence.product;

public class StockUpdateFailed extends RuntimeException {
    public StockUpdateFailed() {
    }

    public StockUpdateFailed(String message) {
        super(message);
    }

    public StockUpdateFailed(String message, Throwable cause) {
        super(message, cause);
    }

    public StockUpdateFailed(Throwable cause) {
        super(cause);
    }

    public StockUpdateFailed(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
