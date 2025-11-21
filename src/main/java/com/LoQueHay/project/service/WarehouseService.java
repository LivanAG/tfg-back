package com.LoQueHay.project.service;

import com.LoQueHay.project.dto.warehouse_dtos.WarehouseRequestDTO;
import com.LoQueHay.project.exception.ResourceNotFoundException;
import com.LoQueHay.project.model.Category;
import com.LoQueHay.project.model.MyUserEntity;
import com.LoQueHay.project.model.Warehouse;
import com.LoQueHay.project.repository.WarehouseRepository;
import com.LoQueHay.project.util.AuthUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final AuthUtils authUtils;

    public WarehouseService(WarehouseRepository warehouseRepository, AuthUtils authUtils) {
        this.warehouseRepository = warehouseRepository;
        this.authUtils = authUtils;
    }

    public Warehouse getById(Long id) {
        return warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found"));
    }


    public Page<Warehouse> getWharehouses(int page, int size, String search) {
        MyUserEntity currentUser = authUtils.getCurrentUser();
        Long ownerId = currentUser.getOwner() != null ? currentUser.getOwner().getId() : currentUser.getId();

        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());

        if (search == null || search.isEmpty()) {
            return warehouseRepository.findByOwnerId(ownerId, pageable);
        } else {
            return warehouseRepository.findByOwnerIdAndNameContainingIgnoreCase(ownerId, search, pageable);
        }
    }

    @Transactional
    public Warehouse create(WarehouseRequestDTO dto) {
        MyUserEntity currentUser = authUtils.getCurrentUser();

        Warehouse warehouse = new Warehouse();
        warehouse.setName(dto.getName());
        warehouse.setLocation(dto.getLocation());
        warehouse.setDescription(dto.getDescription());
        warehouse.setOwner(currentUser.getOwner() != null ? currentUser.getOwner() : currentUser);
        warehouse.setCreatedBy(currentUser);

        return warehouseRepository.save(warehouse);
    }

    public List<Warehouse> getAll() {
        return warehouseRepository.findAll();
    }

    @Transactional
    public Warehouse update(Long id, WarehouseRequestDTO dto) {
        Warehouse existing = this.getById(id);

        existing.setName(dto.getName());
        existing.setLocation(dto.getLocation());
        existing.setDescription(dto.getDescription());
        // owner y createdBy no se modifican

        return warehouseRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        Warehouse existing = this.getById(id);
        warehouseRepository.delete(existing);
    }
}
