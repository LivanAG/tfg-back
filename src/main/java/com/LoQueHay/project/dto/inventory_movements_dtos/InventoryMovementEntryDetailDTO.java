package com.LoQueHay.project.dto.inventory_movements_dtos;

import jakarta.validation.constraints.NotNull;

public class InventoryMovementEntryDetailDTO {
    @NotNull(message = "productId es obligatorio")
    private Long productId;

    @NotNull(message = "quantity es obligatorio")
    private Integer quantity;

    @NotNull(message = "unitCost es obligatorio")
    private Double unitCost;

    private String lotNumber;
    private String expirationDate; // ISO String, opcional

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

    public @NotNull(message = "unitCost es obligatorio") Double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(@NotNull(message = "unitCost es obligatorio") Double unitCost) {
        this.unitCost = unitCost;
    }

    public String getLotNumber() {
        return lotNumber;
    }

    public void setLotNumber(String lotNumber) {
        this.lotNumber = lotNumber;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }
}
