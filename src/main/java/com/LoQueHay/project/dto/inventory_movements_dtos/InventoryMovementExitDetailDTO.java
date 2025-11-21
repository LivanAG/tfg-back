package com.LoQueHay.project.dto.inventory_movements_dtos;

import jakarta.validation.constraints.NotNull;

public class InventoryMovementExitDetailDTO {

    @NotNull(message = "productId es obligatorio")
    private Long productId;

    @NotNull(message = "quantity es obligatorio")
    private Integer quantity;

    @NotNull(message = "sellPriceUnit es obligatorio")
    private Double sellPriceUnit;

    public @NotNull(message = "sellPriceUnit es obligatorio") Double getSellPriceUnit() {
        return sellPriceUnit;
    }

    public void setSellPriceUnit(@NotNull(message = "sellPriceUnit es obligatorio") Double sellPriceUnit) {
        this.sellPriceUnit = sellPriceUnit;
    }

    public @NotNull(message = "productId es obligatorio") Long getProductId() {
        return productId;
    }

    public void setProductId(@NotNull(message = "productId es obligatorio") Long productId) {
        this.productId = productId;
    }

    public @NotNull(message = "quantity es obligatorio") Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(@NotNull(message = "quantity es obligatorio") Integer quantity) {
        this.quantity = quantity;
    }
}
