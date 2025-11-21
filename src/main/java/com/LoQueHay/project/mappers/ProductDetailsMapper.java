package com.LoQueHay.project.mappers;

import com.LoQueHay.project.dto.product_details_dtos.ProductDetailsRequestDTO;
import com.LoQueHay.project.dto.product_details_dtos.ProductDetailsResponseDTO;
import com.LoQueHay.project.model.Product;
import com.LoQueHay.project.model.ProductDetails;

public class ProductDetailsMapper {

    public static ProductDetails toEntity(ProductDetailsRequestDTO dto, Product product){
        ProductDetails details = new ProductDetails();
        details.setWeight(dto.getWeight());
        details.setWidth(dto.getWidth());
        details.setHeight(dto.getHeight());
        details.setDepth(dto.getDepth());
        details.setUnitOfMeasure(dto.getUnitOfMeasure());
        details.setProduct(product);
        return details;
    }

    public static ProductDetailsResponseDTO toDTO(ProductDetails details){
        ProductDetailsResponseDTO dto = new ProductDetailsResponseDTO();
        dto.setId(details.getId());
        dto.setWeight(details.getWeight());
        dto.setWidth(details.getWidth());
        dto.setHeight(details.getHeight());
        dto.setDepth(details.getDepth());
        dto.setUnitOfMeasure(details.getUnitOfMeasure());
        return dto;
    }
}
