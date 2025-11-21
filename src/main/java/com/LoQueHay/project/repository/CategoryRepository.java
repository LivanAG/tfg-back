package com.LoQueHay.project.repository;

import com.LoQueHay.project.model.Category;
import com.LoQueHay.project.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Page<Category> findByOwnerId(Long ownerId, Pageable pageable);
    Page<Category> findByOwnerIdAndNameContainingIgnoreCase(Long ownerId, String name, Pageable pageable);

}

