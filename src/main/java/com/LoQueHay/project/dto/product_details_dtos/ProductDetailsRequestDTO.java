package com.LoQueHay.project.dto.product_details_dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ProductDetailsRequestDTO {

    @NotNull(message = "weight obligatorio")
    @Positive(message = "weight deber ser mayor que cero")
    private Double weight;
    @NotNull(message = "width obligatorio")
    @Positive(message = "width deber ser mayor que cero")
    private Double width;
    @NotNull(message = "height obligatorio")
    @Positive(message = "height deber ser mayor que cero")
    private Double height;
    @NotNull(message = "depth obligatorio")
    @Positive(message = "depth deber ser mayor que cero")
    private Double depth;

    private String unitOfMeasure;

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getDepth() {
        return depth;
    }

    public void setDepth(Double depth) {
        this.depth = depth;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }
}
