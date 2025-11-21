package com.LoQueHay.project.service;

import com.LoQueHay.project.dto.product_details_dtos.ProductDetailsRequestDTO;
import com.LoQueHay.project.exception.DuplicateResourceException;
import com.LoQueHay.project.exception.ResourceNotFoundException;
import com.LoQueHay.project.model.Product;
import com.LoQueHay.project.model.ProductDetails;
import com.LoQueHay.project.repository.ProductDetailsRepository;
import com.LoQueHay.project.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductDetailsService {

    private final ProductDetailsRepository detailsRepository;
    private final ProductService productService;

    public ProductDetailsService(ProductDetailsRepository detailsRepository, ProductService productService) {
        this.detailsRepository = detailsRepository;
        this.productService = productService;
    }

    public ProductDetails getByProduct(Long productId) {
        return detailsRepository.findByProductId(productId).orElseThrow(() -> new ResourceNotFoundException("Product details not found"));
    }

    @Transactional
    public ProductDetails create(Long productId, ProductDetailsRequestDTO dto) {
        Product product = productService.getById(productId);

        Optional<ProductDetails> existingDetails = detailsRepository.findByProductId(productId);
        if(existingDetails.isPresent()){
            throw new DuplicateResourceException("Product details already exists");
        }

        ProductDetails details = new ProductDetails();
        details.setProduct(product);
        details.setWeight(dto.getWeight());
        details.setWidth(dto.getWidth());
        details.setHeight(dto.getHeight());
        details.setDepth(dto.getDepth());
        details.setUnitOfMeasure(dto.getUnitOfMeasure());

        return detailsRepository.save(details);
    }

    @Transactional
    public ProductDetails update(Long productId, ProductDetailsRequestDTO dto) {
        ProductDetails existing = getByProduct(productId);
        if (existing == null) throw new ResourceNotFoundException("Product details not found");

        existing.setWeight(dto.getWeight());
        existing.setWidth(dto.getWidth());
        existing.setHeight(dto.getHeight());
        existing.setDepth(dto.getDepth());
        existing.setUnitOfMeasure(dto.getUnitOfMeasure());

        return detailsRepository.save(existing);
    }

    @Transactional
    public void delete(Long productId) {
        ProductDetails existing = getByProduct(productId);
        if (existing == null) throw new ResourceNotFoundException("Product details not found");
        detailsRepository.delete(existing);
    }
}
