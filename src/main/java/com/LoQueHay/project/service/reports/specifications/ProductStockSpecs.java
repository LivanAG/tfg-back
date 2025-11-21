package com.LoQueHay.project.service.reports.specifications;

import com.LoQueHay.project.model.ProductStock;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class ProductStockSpecs {

    /**
     * @param ownerId     obligatorio
     * @param warehouseId opcional
     * @param categoryId  opcional
     * @param expiringOnly si true filtra solo stocks próximos a vencer
     */
    public static Specification<ProductStock> filterStocks(
            Long ownerId,
            Long warehouseId,
            Long categoryId,
            boolean expiringOnly
    ) {
        return (root, query, cb) -> {
            // JOIN con producto y categoría
            Join<Object, Object> product = root.join("product", JoinType.INNER);
            Join<Object, Object> category = product.join("category", JoinType.INNER);

            // Predicados dinámicos
            Predicate predicate = cb.conjunction();

            // Filtrar por owner a través del producto (obligatorio)
            predicate = cb.and(predicate, cb.equal(product.get("owner").get("id"), ownerId));

            // Filtrar por almacén si se pasa
            if (warehouseId != null) {
                predicate = cb.and(predicate, cb.equal(root.get("warehouse").get("id"), warehouseId));
            }

            // Filtrar por categoría si se pasa
            if (categoryId != null) {
                predicate = cb.and(predicate, cb.equal(category.get("id"), categoryId));
            }

            // Filtrar por fecha de expiración solo si se desea
            if (expiringOnly) {
                predicate = cb.and(
                        predicate,
                        cb.between(root.get("expirationDate"), LocalDate.now(), LocalDate.now().plusDays(30))
                );
            }

            return predicate;
        };
    }
}
