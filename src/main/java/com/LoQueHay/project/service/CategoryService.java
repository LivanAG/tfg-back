package com.LoQueHay.project.service;

import com.LoQueHay.project.dto.category_dtos.CategoryRequestDTO;
import com.LoQueHay.project.exception.ResourceNotFoundException;
import com.LoQueHay.project.model.Category;
import com.LoQueHay.project.model.MyUserEntity;
import com.LoQueHay.project.repository.CategoryRepository;
import com.LoQueHay.project.util.AuthUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final AuthUtils authUtils;

    public CategoryService(CategoryRepository categoryRepository, AuthUtils authUtils) {
        this.categoryRepository = categoryRepository;
        this.authUtils = authUtils;
    }

    public Page<Category> getCategories(int page, int size, String search) {
        MyUserEntity currentUser = authUtils.getCurrentUser();
        Long ownerId = currentUser.getOwner() != null ? currentUser.getOwner().getId() : currentUser.getId();

        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());

        if (search == null || search.isEmpty()) {
            return categoryRepository.findByOwnerId(ownerId, pageable);
        } else {
            return categoryRepository.findByOwnerIdAndNameContainingIgnoreCase(ownerId, search, pageable);
        }
    }

    public Category getById(Long id){
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    @Transactional
    public Category create(CategoryRequestDTO dto) {
        MyUserEntity currentUser = authUtils.getCurrentUser();

        Category category = new Category();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setOwner(currentUser.getOwner() != null ? currentUser.getOwner() : currentUser);
        category.setCreatedBy(currentUser);

        return categoryRepository.save(category);
    }

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Transactional
    public Category update(Long id, CategoryRequestDTO dto) {
        Category existing = this.getById(id);

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());

        return categoryRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        Category existing = this.getById(id);
        categoryRepository.delete(existing);
    }
}
