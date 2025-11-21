package com.LoQueHay.project.exception;


public class InsufficientStockException extends RuntimeException {

    private final Long productId;


    public InsufficientStockException(Long productId) {
        super("No hay suficiente stock para el producto " + productId);
        this.productId = productId;

    }

    public Long getProductId() { return productId; }

}